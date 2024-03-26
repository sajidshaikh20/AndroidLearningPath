package com.base.hilt.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.base.hilt.CreateAccountMutation
import com.base.hilt.base.BaseRepository
import com.base.hilt.network.ResponseHandler
import com.base.hilt.type.SignUpInput
import javax.inject.Inject


class UserRepository @Inject constructor (
    val apolloClient: ApolloClient
) : BaseRepository() {

    suspend fun onSignUpApi(input: SignUpInput): ResponseHandler<ApolloResponse<CreateAccountMutation.Data>> {
        return graphQlApiCall {
            apolloClient.mutation(CreateAccountMutation(input)).execute()
        }
    }
}