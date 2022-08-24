package com.thanosfisherman.grpcplayground.presentation.adapters

import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thanosfisherman.grpcplayground.databinding.ListItemBinding
import com.thanosfisherman.grpcplayground.domain.models.DCredential
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*


class CredentialsHolder private constructor(
    private val binding: ListItemBinding,
    private val clicksChannel: Channel<DCredential>,
    private val coroutineScope: CoroutineScope
) : RecyclerView.ViewHolder(binding.root) {

    fun bindItem(credentials: DCredential) {
        binding.credentialTitle.text = credentials.title
        itemView.clicks().onEach { clicksChannel.send(credentials) }.launchIn(coroutineScope)
    }

    companion object {
        fun from(
            parent: ViewGroup,
            clicksChannel: Channel<DCredential>,
            coroutineScope: CoroutineScope
        )
                : CredentialsHolder {

            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemBinding.inflate(layoutInflater, parent, false)
            return CredentialsHolder(binding, clicksChannel, coroutineScope)
        }
    }
}

private fun View.clicks(): Flow<Unit> = callbackFlow {
    check(Looper.myLooper() == Looper.getMainLooper()) {
        "Expected to be called on the main thread but was " + Thread.currentThread().name
    }
    val listener = View.OnClickListener {
        trySend(Unit)
    }
    setOnClickListener(listener)
    awaitClose { setOnClickListener(null) }
}.conflate()
