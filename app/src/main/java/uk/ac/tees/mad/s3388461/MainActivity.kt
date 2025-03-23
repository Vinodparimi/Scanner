package uk.ac.tees.mad.s3388461

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import uk.ac.tees.mad.s3388461.navigation.MyAppNavigation
import uk.ac.tees.mad.s3388461.ui.theme.ScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScannerTheme {
                MyAppNavigation()
            }
        }
    }
}
