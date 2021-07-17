package br.com.mrocigno.horizonlivemap.core.helpers

import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.lang.Exception

private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

fun picasso(url: String) = PicassoHelper(url)

fun ImageView.load(url: String) = PicassoHelper(url).into(this)
fun ImageView.load(url: String, onEnd: (e: Exception?) -> Unit) = PicassoHelper(url).into(this, onEnd)

class PicassoHelper(private val url: String) {

    private val picasso
        get() = Picasso.get().load(url)

    fun get(creator: RequestCreator = picasso) = creator.get()

    fun getOrNull(creator: RequestCreator = picasso) = runCatching { get(creator) }.getOrNull()

    fun getCached(): Bitmap? = runBlocking {
        val channel = Channel<Boolean>()
        val response = withTimeoutOrNull(4000) {
            picasso.fetch(object : Callback {
                override fun onSuccess() {
                    scope.launch { channel.send(true) }
                }

                override fun onError(e: Exception?) {
                    scope.launch { channel.send(false) }
                }
            })
            channel.receive()
        }
        if (response != false) getOrNull() else null
    }

    fun into(imageView: ImageView) =
        into(imageView) { e -> e?.printStackTrace() }

    fun into(imageView: ImageView, onEnd: (e: Exception?) -> Unit) {
        picasso.into(imageView, object : Callback {
            override fun onSuccess() {
                onEnd(null)
            }

            override fun onError(e: Exception?) {
                onEnd(e)
            }
        })
    }
}
