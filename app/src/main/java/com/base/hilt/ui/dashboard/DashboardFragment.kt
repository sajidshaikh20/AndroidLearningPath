package com.base.hilt.ui.dashboard

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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

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

    override fun initializeScreenVariables() {
       // consumer()
       // consumer1()


        getDataBinding().apply {
            txtgetFormated.setOnClickListener {
                getFormatedNotes()
            }

            sharedflowstart.setOnClickListener {
                GlobalScope.launch(Dispatchers.Main){
                    val result = singleProducerButMultipleConsumer()
                    result.collect{
                        Log.i("sharedflow", "first consumer: item   $it ")
                    }
                }
                GlobalScope.launch(Dispatchers.Main){
                    val result = singleProducerButMultipleConsumer()
                    delay(2500)
                    result.collect{
                        Log.i("sharedflow", "second consumer: item   $it ")
                    }
                }
            }
            flowstart.setOnClickListener {
                consumer()
            }
            flowstarte.setOnClickListener {
                consumer1()
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
        GlobalScope.launch {
            val data: Flow<Int> = producer()
            data.onStart {
                emit(-1)
                Log.i("2181", "consumer: Consumer start")
            }.onCompletion {
                emit(10)
                Log.i("2181", "consumer: Consumer on complete ")
            }.onEach {
                Log.i("2181", "on about emit value $it")
            }.buffer(2)
                .collect {
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
        val mutableSharedFlow = MutableSharedFlow<Int>()
        GlobalScope.launch(Dispatchers.Main) {
            val movies = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
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
             } catch (e:Exception){
                 Log.i("error", "consumer ${e.toString()}")
             }

        }
    }
}