package com.hecosw.trailtracker.ui.screen.trail

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.hecosw.trailtracker.R

@Composable
fun SystemLocationEnablingRequestDialog(onDismissed: () -> Unit) {
    AlertDialog(
        onDismissRequest = {
            onDismissed()
        },
        title = {
            Text(stringResource(id = R.string.system_location_enabling_request_dialog_title))
        },
        text = {
            Text(stringResource(id = R.string.system_location_enabling_request_dialog_text))
        },
        confirmButton = {
            Button(onClick = { onDismissed() }) {
                Text( stringResource(id = R.string.generic_close))
            }
        }
    )
}
