package dev.rhcehd123.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.rhcehd123.data.repository.CurrencyRepository
import dev.rhcehd123.ui.main.Main
import dev.rhcehd123.ui.main.MainViewModel
import dev.rhcehd123.ui.theme.CurrencyInfoTheme
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    //private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var currencyRepository: CurrencyRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            LaunchedEffect(viewModel) {
                viewModel.toastEvent.collect { message ->
                    Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
                }
            }
            CurrencyInfoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Main(viewModel = viewModel)
                }
            }
        }
        /*lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                //currencyRepository.startIntervalRefresh()
                //currencyRepository.getCurrencyData()
                currencyRepository.getDummyData()
            }
        }*/
    }

    override fun onStart() {
        //currencyRepository.startIntervalRefresh()
        //currencyRepository.getCurrencyData()
        currencyRepository.getDummyData()
        super.onStart()
    }

    override fun onStop() {
        currencyRepository.stopIntervalRefresh()
        super.onStop()
    }

    override fun onDestroy() {
        currencyRepository.stopIntervalRefresh()
        super.onDestroy()
    }
}