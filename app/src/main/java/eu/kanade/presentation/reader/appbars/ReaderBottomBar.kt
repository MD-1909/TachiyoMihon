package eu.kanade.presentation.reader.appbars

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import dev.icerock.moko.resources.StringResource
import eu.kanade.tachiyomi.ui.reader.setting.ReaderOrientation
import eu.kanade.tachiyomi.ui.reader.setting.ReadingMode
import mihon.icons.custommaterialsymbols.CustomMaterialSymbols
import mihon.icons.custommaterialsymbols.rounded.CropOff
import mihon.icons.materialsymbols.MaterialSymbols
import mihon.icons.materialsymbols.rounded.Check
import mihon.icons.materialsymbols.rounded.Crop
import mihon.icons.materialsymbols.rounded.Settings
import tachiyomi.i18n.MR
import tachiyomi.presentation.core.i18n.stringResource

@Composable
fun ReaderBottomBar(
    readingMode: ReadingMode,
    onSelectReadingMode: (ReadingMode) -> Unit,
    orientation: ReaderOrientation,
    onSelectOrientation: (ReaderOrientation) -> Unit,
    cropEnabled: Boolean,
    onClickCropBorder: () -> Unit,
    onClickSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .pointerInput(Unit) {},
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SelectMenuButton(
            icon = readingMode.icon,
            contentDescription = stringResource(MR.strings.viewer),
            options = ReadingMode.entries,
            selected = readingMode,
            label = { it.stringRes },
            onSelect = onSelectReadingMode,
        )

        SelectMenuButton(
            icon = orientation.icon,
            contentDescription = stringResource(MR.strings.rotation_type),
            options = ReaderOrientation.entries,
            selected = orientation,
            label = { it.stringRes },
            onSelect = onSelectOrientation,
        )

        IconButton(onClick = onClickCropBorder) {
            Icon(
                imageVector = if (cropEnabled) MaterialSymbols.Rounded.Crop else CustomMaterialSymbols.Rounded.CropOff,
                contentDescription = stringResource(MR.strings.pref_crop_borders),
            )
        }

        IconButton(onClick = onClickSettings) {
            Icon(
                imageVector = MaterialSymbols.Rounded.Settings,
                contentDescription = stringResource(MR.strings.action_settings),
            )
        }
    }
}

// Tachiyomi-style picker: a plain list that applies the choice on tap
@Composable
private fun <T> SelectMenuButton(
    icon: ImageVector,
    contentDescription: String,
    options: List<T>,
    selected: T,
    label: (T) -> StringResource,
    onSelect: (T) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = icon, contentDescription = contentDescription)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(stringResource(label(option))) },
                    leadingIcon = {
                        Icon(
                            imageVector = MaterialSymbols.Rounded.Check,
                            contentDescription = null,
                            modifier = Modifier.alpha(if (option == selected) 1f else 0f),
                        )
                    },
                    onClick = {
                        expanded = false
                        onSelect(option)
                    },
                )
            }
        }
    }
}
