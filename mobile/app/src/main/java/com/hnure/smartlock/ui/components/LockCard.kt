package com.hnure.smartlock.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hnure.smartlock.data.api.LockDto
import com.hnure.smartlock.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun LockCard(
    lock: LockDto,
    onToggle: suspend (String, Boolean) -> Unit,
    onDelete: (LockDto) -> Unit,
    onOpenDetail: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var toggling by remember { mutableStateOf(false) }
    val isLocked = lock.locked
    val isOnline = lock.status == "ONLINE"
    val scope = rememberCoroutineScope()

    val iconBg by animateColorAsState(
        targetValue = if (isLocked) LightBlue else SurfaceLight,
        animationSpec = tween(300),
        label = "iconBg"
    )
    val iconBorder by animateColorAsState(
        targetValue = if (isLocked) BlueBorder else BorderGray,
        animationSpec = tween(300),
        label = "iconBorder"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenDetail(lock.lockId) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Top row: icon + status chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Lock icon box
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(iconBg)
                        .border(1.dp, iconBorder, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = null,
                        tint = if (isLocked) PrimaryBlue else MediumText,
                        modifier = Modifier.size(22.dp)
                    )
                }

                LockStatusChip(status = lock.status)
            }

            Spacer(Modifier.height(12.dp))

            // Lock name
            Text(
                text = lock.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(4.dp))

            // State chip
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = if (isLocked) LightBlue else SurfaceLight,
                contentColor = if (isLocked) PrimaryBlue else MediumText
            ) {
                Text(
                    text = if (isLocked) "Заблоковано" else "Розблоковано",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
            }

            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(Modifier.height(10.dp))

            // Action row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Toggle button
                Button(
                    onClick = {
                        toggling = true
                        scope.launch {
                            try { onToggle(lock.lockId, isLocked) } finally { toggling = false }
                        }
                    },
                    enabled = !toggling,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLocked) PrimaryBlue else Color.Transparent,
                        contentColor = if (isLocked) Color.White else PrimaryBlue
                    ),
                    border = if (!isLocked) androidx.compose.foundation.BorderStroke(1.dp, PrimaryBlue) else null,
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    if (toggling) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp,
                            color = if (isLocked) Color.White else PrimaryBlue
                        )
                    } else {
                        Icon(
                            imageVector = if (isLocked) Icons.Default.LockOpen else Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = if (isLocked) "Відкрити" else "Закрити",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                // Settings icon
                IconButton(
                    onClick = { onOpenDetail(lock.lockId) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Деталі",
                        tint = MediumText,
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Delete icon
                IconButton(
                    onClick = { onDelete(lock) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Видалити",
                        tint = MediumText,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

fun formatDateTime(isoString: String): String {
    return try {
        val instant = java.time.Instant.parse(isoString)
        val formatter = java.time.format.DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm")
            .withZone(java.time.ZoneId.systemDefault())
        formatter.format(instant)
    } catch (e: Exception) {
        isoString
    }
}
