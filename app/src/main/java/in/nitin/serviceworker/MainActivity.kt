package `in`.nitin.serviceworker

import `in`.nitin.serviceworker.databinding.ActivityMainBinding
import `in`.nitin.serviceworker.service.ServiceWorker
import `in`.nitin.serviceworker.service.Task
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response


class MainActivity : AppCompatActivity() {

    var serviceWorker1: ServiceWorker = ServiceWorker("service_worker_1")
    var serviceWorker2: ServiceWorker = ServiceWorker("service_worker_2")

    /**
     * [Note]: here you don't need to change the imageUrl because every time it loads different image.
     * */
    val IMAGE_1 = "https://picsum.photos/200/200"
    var IMAGE_2 = "https://picsum.photos/200/200"

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        binding.button1.setOnClickListener {
            fetchImage1AndSet()
        }

        binding.button2.setOnClickListener {
            fetchImage2AndSet()
        }
    }

    private fun fetchImage1AndSet() {
        serviceWorker1.addTask(object : Task<Bitmap> {
            override fun onExecute(): Bitmap {

                return getBitmapFromUrl(IMAGE_1);
            }

            override fun onTaskComplete(result: Bitmap) {

                binding.imageView1.setImageBitmap(result);

            }

            override fun onError(exception: Exception?) {
                exception!!.message!!.showSnackBar(Color.RED)
            }
        })
    }

    private fun fetchImage2AndSet() {
        serviceWorker2.addTask(object : Task<Bitmap> {
            override fun onExecute(): Bitmap {

                return getBitmapFromUrl(IMAGE_2);
            }

            override fun onTaskComplete(result: Bitmap) {

                binding.imageView2.setImageBitmap(result);
            }

            override fun onError(exception: Exception?) {
                exception!!.message!!.showSnackBar(Color.RED)

            }


        })
    }

    /**
     * @param imageUrl: getting bitmap from url using retrofit
     * */
    private fun getBitmapFromUrl(imageUrl: String): Bitmap {
        val okHttpClient = OkHttpClient()
        val request: Request = Request.Builder().url(imageUrl).build()
        val response: Response = okHttpClient.newCall(request).execute()
        return BitmapFactory.decodeStream(response.body()!!.byteStream())
    }

    /**
     * @param color: snackBar background color
     * @param String: message to show
     * */
    private fun String.showSnackBar(color: Int) {
        Snackbar.make(binding.root, this, Snackbar.LENGTH_LONG).apply {
            this.setBackgroundTint(color)
        }.show()
    }

    /**
     * service stops when activity destroy
     * */
    override fun onDestroy() {
        super.onDestroy()
        serviceWorker1.stop()
        serviceWorker2.stop()
    }

}
