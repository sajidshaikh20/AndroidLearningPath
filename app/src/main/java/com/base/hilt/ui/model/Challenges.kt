package com.base.hilt.ui.model


data class Challenges(
    var uuid: String? = null,
    var title: String? = null,
    var description: String? = null,
    var image: List<String?>? = null,
    var type: String? = null,
    var amount: String? = null,
    var jackpot_amount: String? = null,
    var start_at: String? = null,
    var accept_by: String? = null,
    var end_at: String? = null,
    var is_judge: Boolean? = null,
    var is_author: Boolean? = null,
    var is_winner: Boolean? = null,
    var is_spectator: Boolean? = null,
    var author: UserData? = null,
    var judge: UserData? = null,
    var participants: List<UserData?>? = null,
    var winner: UserData? = null,
    var winner_declare_by: String? = null,
    var winner_declare_at: String? = null,
    var allow_to_edit: String? = null,
    var status: String? = null,
    var invite_status: String? = null,
    var challenge_status: String? = null,
    var modification_request: ModificationData? = ModificationData(),
    var total_spectators: Int? = null,
    var total_participants: Int? = null,
    var timestamp: String? = null,
    var is_participant: Boolean? = null,
    var is_ended: Boolean? = null,
    var current_date: String? = null,
    var other_declined: Boolean? = null,
    var invitation_status_label: String? = null,
){
    data class ModificationData(
        var uuid: String? = "",
        var challenge_id: String? = "",
        var amount: String? = "",
        var description: String? = "",
        var judge: UserData? = null,
        var invitation_accept_date: String? = "",
        var end_at: String? = "",
        var amount_approved: String? = "",
        var description_approved: String? = "",
        var judge_id_approved: String? = "",
        var invitation_accept_date_approved: String? = "",
        var end_at_approved: String? = "",
        var status: String? = "",
        var action_on: String? = "",
    )

    data class UserData(
        var uuid: String? = null,
        var first_name: String? = null,
        var last_name: String? = null,
        var avatar: String? = null,
        var invite_status: String? = null,
        var phone: List<String>? = null,
        var isSelected: Boolean? = false,
        val status: String? = null,
        val id: String? = null,
        val verified: String? = null,
        val is_block: Int? = null,
        val chatId: String? = null,
        val chat_user_id: String? = null,
        var isEditable: Boolean? = true,
    )


    var endingOn:String = "ENDING ON: "+ this.end_at
    var acceptyBy:String = "Accept By: "+ this.accept_by
    var endBy:String = "End By: "+ this.end_at
    var challengeType:String = this.type+" Challenge"
    var noParticipants:String = this.total_participants.toString()+" Participants"
    var noSpectators:String = this.total_spectators.toString()+" Spectators"
    val authorName = this.author?.first_name.toString()+" "+this.author?.last_name.toString()

    fun type1v1():Boolean{
        return this.type=="1v1"
    }
    fun typeGroup():Boolean{
        return this.type=="group"
    }

}