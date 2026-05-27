package com.hnure.smartlock.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hnure.smartlock.ui.theme.*

@Composable
fun LockStatusChip(status: String, modifier: Modifier = Modifier) {
    val isOnline = status == "ONLINE"
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = if (isOnline) GreenBg else SurfaceLight,
        contentColor = if (isOnline) GreenSuccess else MediumText,
        border = BorderStroke(1.dp, if (isOnline) GreenBorder else BorderGray),
        modifier = modifier
    ) {
        Text(
            text = status,
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}

@Composable
fun RoleChip(role: String, modifier: Modifier = Modifier) {
    val (bg, fg, border) = when (role) {
        "OWNER"  -> Triple(LightBlue, OwnerBlue, BlueBorder)
        "ADMIN"  -> Triple(LightBlue, AdminBlue, BlueBorder)
        "MEMBER" -> Triple(GreenBg, MemberGreen, GreenBorder)
        else     -> Triple(SurfaceLight, GuestGray, BorderGray)
    }
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = bg,
        contentColor = fg,
        border = BorderStroke(1.dp, border),
        modifier = modifier
    ) {
        Text(
            text = role,
            style = androidx.compose.material3.MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        )
    }
}
