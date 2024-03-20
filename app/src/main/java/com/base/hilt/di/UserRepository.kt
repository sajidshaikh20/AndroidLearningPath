package com.base.hilt.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.FeedResultQuery
import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ResponseHandler
import javax.inject.Inject


//class UserRepository @Inject constructor (
//    val apolloClient: ApolloClient
//) : BaseRepository() {
//
//    suspend fun onFeedResultFetchApi(): ResponseHandler<ApolloResponse<FeedResultQuery.Data>> {
//        return graphQlApiCall {
//            apolloClient.query(FeedResultQuery()).execute()
//        }
//    }
//
//
//
//}