package com.thanosfisherman.grpcplayground.presentation.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.thanosfisherman.grpcplayground.domain.models.DCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow


class CredentialsRecyclerAdapter(private val coroutineScope: CoroutineScope) :
    ListAdapter<DCredential, CredentialsHolder>(
        CredentialsDiffCallback()
    ) {

    private val itemClicksChannel: Channel<DCredential> = Channel(Channel.CONFLATED)
    val itemClicks: Flow<DCredential> = itemClicksChannel.receiveAsFlow()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CredentialsHolder {
        return CredentialsHolder.from(parent, itemClicksChannel, coroutineScope)
    }

    override fun onBindViewHolder(holder: CredentialsHolder, position: Int) {
        getItem(position)?.let { holder.bindItem(it) }
    }

    private class CredentialsDiffCallback : DiffUtil.ItemCallback<DCredential>() {
        override fun areItemsTheSame(oldItem: DCredential, newItem: DCredential): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DCredential, newItem: DCredential): Boolean {
            return oldItem == newItem
        }
    }
}
