package com.base.hilt.ui.dashboard

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.base.hilt.R
import com.base.hilt.base.FragmentBase
import com.base.hilt.base.ToolbarModel
import com.base.hilt.databinding.FragmentDashboardBinding
import com.base.hilt.ui.dashboard.model.FormatedNote
import com.base.hilt.ui.dashboard.model.Note
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.withIndex
import kotlinx.coroutines.launch
import java.io.IOException

@AndroidEntryPoint
class DashboardFragment : FragmentBase<DashboardViewModel, FragmentDashboardBinding>() {
    // Using a Channel for demonstration purposes
    val channel = Channel<Int>()

    // Declaring a nullable Job for global coroutine management
    var globalJob: Job? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_dashboard
    }

    override fun setupToolbar() {
        viewModel.setToolbarItems(ToolbarModel(true, "Flow DashBoard", true))
    }

    @SuppressLint("SetTextI18n")
    override fun initializeScreenVariables() {

        debounceSearchQueries()
        getDataBinding().Cancel.setOnClickListener {
            Toast.makeText(requireContext(), "cancel", Toast.LENGTH_SHORT).show()
            globalJob?.cancel()
        }
        getDataBinding().apply {
            txtgetFormated.setOnClickListener {
                getFormatedNotes()
            }
            flatMapConcat.setOnClickListener {
                flatmapConcate()
            }
            //shared flow
            sharedflowstart.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main) {
                    val result = singleProducerButMultipleConsumer()
                    result.collect {
                        Log.i("sharedflow", "first consumer: item   $it ")
                        getDataBinding().txtconsumer.text = "First Consumer" + it.toString()
                    }
                }
                GlobalScope.launch(Dispatchers.Main) {
                    val result = singleProducerButMultipleConsumer()
                    delay(2500)
                    result.collect {
                        Log.i("sharedflow", "second consumer: item   $it ")
                    }
                }
            }
            //first consumer
            flowstart.setOnClickListener {
                consumer()
            }
            //Second Consumer
            flowstarte.setOnClickListener {
                consumer1()
            }
            //stateFlow
            StateFlow.setOnClickListener {

                GlobalScope.launch {
                    val data: Flow<Int> = StateFlow()
                    data.collect {
                        Log.i("2181", "state flow: $it")
                    }
                }
            }

        }

        //channel start
        getDataBinding().channelstart.setOnClickListener {
            Toast.makeText(requireContext(), "start check log", Toast.LENGTH_SHORT).show()
            producerChannel()
            GlobalScope.launch {
                delay(5000)
                consumerchannel()
            }
        }

    }


    override fun getViewModelClass(): Class<DashboardViewModel> = DashboardViewModel::class.java


    //Consumer to consume flow data
    fun consumer() {
        globalJob = GlobalScope.launch {

            val data: Flow<Int> = producer()

            data.onStart {
                emit(-1)
                Log.i("2181", "consumer: Consumer start")
            }.onCompletion {
                    emit(10)
                    Log.i("2181", "consumer: Consumer on complete ")
                }.onEach {
                    Log.i("2181", "on about emit value $it")
                }.collect {
                    Log.i("2181", "Consumer first : $it")
                }
        }
    }


    /*
    * thier are two types of operator in flow
    * 1:- terminal operator
    * 2:- Non terminal operator
    *
    * terminal operator :- which allow to consumtion of our flow Always terminal operator is suspend function
    * */


    //this consumer for terminal create for terminal operator
    fun consumer1() {
        GlobalScope.launch {
            val data: Flow<Int> = producer()
            val first = data.first()
            //it will run after all value is emit
            val list = data.toList()
            val list2 = producer().toList()
            val datawithindex = data.withIndex().collect()
            Log.i("2181", "data with index: ${datawithindex}")
            Log.i("2181", "consumer first  terminal operator: $first")
            Log.i("2181", "consumer tolist  terminal operator: $list")
            Log.i("2181", "consumer tolist  terminal operator: $list2")
        }
    }

    //Flow producer which return flow single object
    fun producer() = flow<Int> {
        val list = listOf<Int>(1, 2, 3, 4, 5, 6, 7)
        list.forEach {
            emit(it)
            delay(2000)
        }

    }

    //demo for share flow
    private fun singleProducerButMultipleConsumer(): Flow<Int> {
        //replay bhi ker sakte hai
        val mutableSharedFlow = MutableSharedFlow<Int>(2)
        GlobalScope.launch(Dispatchers.Main) {
            val movies = listOf(1, 2, 3, 4, 5)
            movies.forEach {
                mutableSharedFlow.emit(it)
                delay(1000)
            }
        }
        return mutableSharedFlow
    }

    // Function to consume values from the channel
    fun consumerchannel() {
        CoroutineScope(Dispatchers.Main).launch {
            Log.i("2181", "consumer: ${channel.receive().toString()}")
            Log.i("2181", "consumer: ${channel.receive().toString()}")
            Log.i("2181", "consumer: ${channel.receive().toString()}")
            Log.i("2181", "consumer: ${channel.receive().toString()}")
            Log.i("2181", "consumer: ${channel.receive().toString()}")
        }
    }

    fun producerChannel() {
        CoroutineScope(Dispatchers.Main).launch {
            channel.send(1)
            channel.send(2)
            channel.send(3)
            channel.send(4)
            channel.send(5)
        }
    }

    private suspend fun getUserId(id: String): String {
        delay(1000)
        return "USER$id"
    }


    private suspend fun getNotes(): Flow<Note> {
        return flow {
            val list = listOf(
                Note(1, true, "first", "first Description"),
                Note(2, false, "second", "second Description"),
                Note(3, true, "third", "third Description"),
                Note(4, false, "fourth", "fourth Description"),
                Note(5, true, "fifth", "fifth Description"),
                Note(6, true, "sixth", "sixth Description"),
                Note(7, false, "seven", "seven Description"),
                Note(8, true, "eight", "eight Description"),
                Note(9, false, "nine", "nine Description"),
            )
            list.asFlow()
            list.forEach {
                delay(1000)
                emit(it)
                Log.i("2181", "producer: ${Thread.currentThread().name}")
            }

        }.catch {
            Log.i("2182", "error: $it")
        }
    }

    private fun getFormatedNotes() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                getNotes().map {
                    FormatedNote(it.isActive, it.title.uppercase(), it.description)
                }.filter {
                    it.isActive
                }.onCompletion {
                    Log.i("2181", "consumer: ${Thread.currentThread().name}")
                }.flowOn(Dispatchers.Main)
                    .collect {
                        Log.i("2181", "collector: ${Thread.currentThread().name}")
                        Log.i("2181", "getuser id: $it")
                    }
            } catch (e: Exception) {
                Log.i("error", "consumer ${e.toString()}")
            }

        }
    }

    /* StateFlow is a state-holder observable flow that emits the current and new state
      updates to its collectors. The current state value can also be read through its
      value property. To update state and send it to the flow, assign a new value to the
      value property of the MutableStateFlow class.*/
    private fun StateFlow(): Flow<Int> {
        val mutableStateFlow = MutableStateFlow<Int>(1)
        GlobalScope.launch(Dispatchers.Main) {
            delay(2000)
            mutableStateFlow.emit(2)
            delay(2000)
            mutableStateFlow.emit(3)
        }
        return mutableStateFlow
    }


    /*The flatMapConcat operator is used to transform each value from the
    source flow into a new flow, and then concatenates these flows to
    create a single output flow. The syntax is as follows*/
    private fun flatmapConcate() {
        val numbersFlow = flowOf(1, 2, 3)
        val stringsFlow = numbersFlow.flatMapConcat { number ->
            flowOf("$number First", "$number Second", "$number third")
        }
        GlobalScope.launch(Dispatchers.Main) {
            stringsFlow.collect {
                println(it)
                Log.i("2181", "$it")
            }
        }

    }

    private fun debounceSearchQueries() {
        val debounceTime = 500L
        val searchFlow = MutableSharedFlow<String>()
        // Simulating user input
        CoroutineScope(Dispatchers.Main).launch {
            searchFlow
                .debounce(debounceTime)
                .collect { query ->
                    // Perform search operation here
                    println("Searching for: $query")
                    Log.i("2181", "Searching for: $query")
                    // Simulate network request
                    delay(1000)
                    println("Search completed for: $query")
                    Log.i("2181", "Search completed for $query")
                }
        }

        // Simulating user input
        repeat(2) { index ->
            val query = "Query $index"
            CoroutineScope(Dispatchers.Main).launch {
                delay(index * 200L) // Simulating user typing delay
                searchFlow.emit(query)
            }
        }
    }


    // Function to simulate a network call with potential failure
    suspend fun performNetworkRequest(): String {
        // Simulating a network request
        delay(1000)
        // Simulating network error (IOException)
        throw IOException("Network error occurred")
        // In a real scenario, you would perform the actual network request here
    }

    // Function to create a retry flow for the network request
    fun retryNetworkRequest(): Flow<String> = flow {
        // Retry logic with a maximum of 3 attempts
        emit(performNetworkRequest())
    }.retry(3) { cause ->
        // Retry only if the cause of failure is IOException
        cause is IOException
    }
}
