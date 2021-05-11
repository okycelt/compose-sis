package cz.okycelt.composesavedinstancestate

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.google.android.material.button.MaterialButton
import cz.okycelt.composesavedinstancestate.ui.theme.ComposeSavedInstanceStateTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeSavedInstanceStateTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "first"
                    ) {
                        composable("first") {
                            FirstScreen(
                                navigateToSecondScreen = { navController.navigate("second") }
                            )
                        }

                        composable("second") {
                            SecondScreen(
                                navigateBack = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FirstScreen(
    navigateToSecondScreen: () -> Unit
) {
    val timestamp = rememberSaveable { System.currentTimeMillis() }

    AndroidView(
        factory = {
            TestLinearLayout(it).apply {
                button.setOnClickListener {
                    setBackgroundResource(android.R.color.holo_blue_light)
                }
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = {
            it.textView.text = timestamp.toString()
            it.button2.setOnClickListener {
                navigateToSecondScreen()
            }
        }
    )
}

@Composable
fun SecondScreen(
    navigateBack: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(
            onClick = navigateBack
        ) {
            Text(text = "Go back")
        }
    }
}

class TestLinearLayout(context: Context) : LinearLayout(context) {
    val button: MaterialButton
    val button2: MaterialButton
    val textView: AppCompatTextView

    init {
        gravity = Gravity.CENTER
        orientation = VERTICAL

        textView = AppCompatTextView(context)
        textView.id = R.id.text_view_id
        addView(textView)

        button = MaterialButton(context)
        button.text = "Change background"
        addView(button)

        button2 = MaterialButton(context)
        button2.text = "Navigate to second screen"
        addView(button2)
    }

    override fun onSaveInstanceState(): Parcelable? {
        Log.d("TestLinearLayout", "onSaveInstanceState()")
        return super.onSaveInstanceState()
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        Log.d("TestLinearLayout", "onRestoreInstanceState()")
        super.onRestoreInstanceState(state)
    }
}