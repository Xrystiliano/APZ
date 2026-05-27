package com.hnure.smartlock.ui.screens.lockdetail

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hnure.smartlock.data.api.AccessKeyDto
import com.hnure.smartlock.data.api.LockDto
import com.hnure.smartlock.data.api.LockRoleDto
import com.hnure.smartlock.ui.components.ConfirmDialog
import com.hnure.smartlock.ui.components.LockStatusChip
import com.hnure.smartlock.ui.components.RoleChip
import com.hnure.smartlock.ui.components.formatDateTime
import com.hnure.smartlock.ui.theme.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LockDetailScreen(
    lockId: String,
    onNavigateBack: () -> Unit,
    onLockDeleted: () -> Unit,
    viewModel: LockDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var isRenaming by remember { mutableStateOf(false) }
    var renameText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(lockId) { viewModel.load(lockId) }

    LaunchedEffect(state.isDeleted) {
        if (state.isDeleted) onLockDeleted()
    }

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    if (isRenaming) {
                        OutlinedTextField(
                            value = renameText,
                            onValueChange = { renameText = it },
                            modifier = Modifier.fillMaxWidth().padding(end = 8.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(8.dp),
                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderGray)
                        )
                    } else {
                        Text(
                            text = state.lock?.name ?: "",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Назад")
                    }
                },
                actions = {
                    if (isRenaming) {
                        IconButton(onClick = {
                            if (renameText.isNotBlank()) viewModel.renameLock(lockId, renameText.trim())
                            isRenaming = false
                        }) { Icon(Icons.Default.Check, null, tint = GreenSuccess) }
                        IconButton(onClick = { isRenaming = false }) {
                            Icon(Icons.Default.Close, null, tint = MediumText)
                        }
                    } else {
                        if (state.canManage) {
                            IconButton(onClick = { renameText = state.lock?.name ?: ""; isRenaming = true }) {
                                Icon(Icons.Default.Edit, "Перейменувати", tint = MediumText)
                            }
                        }
                        if (state.myRole == "OWNER") {
                            IconButton(onClick = { showDeleteDialog = true }) {
                                Icon(Icons.Default.Delete, "Видалити", tint = ErrorRed)
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = CardWhite)
            )
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        if (state.isLoading) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
            return@Scaffold
        }

        val lock = state.lock
        if (lock == null) {
            Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Замок не знайдено", color = MediumText)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = onNavigateBack) { Text("Назад") }
                }
            }
            return@Scaffold
        }

        Column(Modifier.fillMaxSize().padding(paddingValues)) {
            // Control Panel
            LockControlPanel(
                lock = lock,
                myRole = state.myRole,
                onToggle = { viewModel.toggleLock(lockId, lock.locked) }
            )

            // Tab row
            val tabs = buildList {
                add("Інфо")
                add("Користувачі (${state.roles.size})")
                if (state.canManage) add("Ключі (${state.keys.size})")
            }

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = CardWhite,
                contentColor = PrimaryBlue,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimaryBlue
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                style = MaterialTheme.typography.labelLarge,
                                color = if (selectedTab == index) PrimaryBlue else MediumText
                            )
                        }
                    )
                }
            }

            // Tab content
            when (selectedTab) {
                0 -> InfoTab(lock)
                1 -> RolesTab(
                    roles = state.roles,
                    myRole = state.myRole,
                    myEmail = state.myEmail,
                    canManage = state.canManage,
                    onAdd = { email, role -> viewModel.addUser(lockId, email, role) },
                    onRemove = { email -> viewModel.removeUser(lockId, email) },
                    onChangeRole = { email, role -> viewModel.changeRole(lockId, email, role) }
                )
                2 -> if (state.canManage) KeysTab(
                    keys = state.keys,
                    myRole = state.myRole,
                    onGenerate = { from, until, onToken -> viewModel.generateKey(lockId, from, until, onToken) },
                    onRevoke = { keyId -> viewModel.revokeKey(keyId) }
                )
            }
        }
    }

    if (showDeleteDialog) {
        ConfirmDialog(
            title = "Видалити замок",
            message = "Видалити замок \"${state.lock?.name}\"? Цю дію не можна скасувати.",
            onConfirm = { viewModel.deleteLock(lockId); showDeleteDialog = false },
            onDismiss = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun LockControlPanel(lock: LockDto, myRole: String, onToggle: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardWhite,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Big lock icon
            val isLocked = lock.locked
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(if (isLocked) LightBlue else SurfaceLight)
                    .border(2.dp, if (isLocked) BlueBorder else BorderGray, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                    contentDescription = null,
                    tint = if (isLocked) PrimaryBlue else MediumText,
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(Modifier.weight(1f)) {
                Text(
                    text = if (lock.locked) "Заблоковано" else "Розблоковано",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (lock.locked) "Доступ обмежено" else "Доступ відкрито",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MediumText
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    LockStatusChip(lock.status)
                    RoleChip(myRole)
                }
            }
        }
        HorizontalDivider(color = BorderGray)
    }
}

@Composable
private fun InfoTab(lock: LockDto) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                InfoRow("Ідентифікатор", lock.lockId)
                HorizontalDivider(color = BorderLight)
                InfoRow("Статус зв'язку", lock.status)
                HorizontalDivider(color = BorderLight)
                InfoRow("Стан замку", if (lock.locked) "Заблоковано" else "Розблоковано")
                HorizontalDivider(color = BorderLight)
                InfoRow("Останній сигнал", lock.lastHeartBeatAt?.let { formatDateTime(it) } ?: "—")
                if (!lock.serialNumber.isNullOrEmpty()) {
                    HorizontalDivider(color = BorderLight)
                    InfoRow("Серійний номер", lock.serialNumber)
                }
                if (!lock.timezone.isNullOrEmpty()) {
                    HorizontalDivider(color = BorderLight)
                    InfoRow("Часовий пояс", lock.timezone)
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MediumText, modifier = Modifier.weight(1f))
        Spacer(Modifier.width(8.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1.5f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RolesTab(
    roles: List<LockRoleDto>,
    myRole: String,
    myEmail: String,
    canManage: Boolean,
    onAdd: (String, String) -> Unit,
    onRemove: (String) -> Unit,
    onChangeRole: (String, String) -> Unit
) {
    val ROLES = listOf("GUEST", "MEMBER", "ADMIN")
    var inviteEmail by remember { mutableStateOf("") }
    var inviteRole by remember { mutableStateOf("GUEST") }
    var inviting by remember { mutableStateOf(false) }
    var editingEmail by remember { mutableStateOf<String?>(null) }
    var editRole by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Invite panel
        if (canManage) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite),
                elevation = CardDefaults.cardElevation(0.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Запросити користувача", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = inviteEmail,
                        onValueChange = { inviteEmail = it },
                        label = { Text("Email") },
                        placeholder = { Text("user@example.com", color = MediumText) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderGray)
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        var roleExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = roleExpanded,
                            onExpandedChange = { roleExpanded = it },
                            modifier = Modifier.weight(1f)
                        ) {
                            OutlinedTextField(
                                value = inviteRole,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Роль") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderGray)
                            )
                            ExposedDropdownMenu(expanded = roleExpanded, onDismissRequest = { roleExpanded = false }) {
                                ROLES.forEach { role ->
                                    DropdownMenuItem(
                                        text = { Text(role) },
                                        onClick = { inviteRole = role; roleExpanded = false }
                                    )
                                }
                            }
                        }
                        Button(
                            onClick = {
                                if (inviteEmail.isNotBlank()) {
                                    inviting = true
                                    onAdd(inviteEmail.trim(), inviteRole)
                                    inviteEmail = ""; inviteRole = "GUEST"; inviting = false
                                }
                            },
                            enabled = !inviting && inviteEmail.isNotBlank(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            modifier = Modifier.height(56.dp)
                        ) {
                            if (inviting) CircularProgressIndicator(Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                            else { Icon(Icons.Default.PersonAdd, null, Modifier.size(16.dp)); Spacer(Modifier.width(4.dp)); Text("Додати") }
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        // Roles list
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
        ) {
            if (roles.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Немає користувачів", color = MediumText)
                }
            } else {
                Column {
                    roles.forEachIndexed { i, role ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Avatar
                            Box(
                                Modifier.size(36.dp).clip(CircleShape).background(LightBlue),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    role.email.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = PrimaryBlue,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.width(12.dp))
                            Column(Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(role.email, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f, fill = false))
                                    if (role.email.equals(myEmail, ignoreCase = true)) {
                                        Spacer(Modifier.width(4.dp))
                                        Surface(shape = RoundedCornerShape(4.dp), color = LightBlue, contentColor = PrimaryBlue) {
                                            Text("Ви", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                    }
                                }
                                Spacer(Modifier.height(2.dp))
                                if (editingEmail == role.email) {
                                    var roleExpanded by remember { mutableStateOf(false) }
                                    ExposedDropdownMenuBox(expanded = roleExpanded, onExpandedChange = { roleExpanded = it }) {
                                        OutlinedTextField(
                                            value = editRole, onValueChange = {}, readOnly = true,
                                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleExpanded) },
                                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                                            shape = RoundedCornerShape(6.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderGray)
                                        )
                                        ExposedDropdownMenu(expanded = roleExpanded, onDismissRequest = { roleExpanded = false }) {
                                            ROLES.forEach { r ->
                                                DropdownMenuItem(text = { Text(r) }, onClick = { editRole = r; roleExpanded = false })
                                            }
                                        }
                                    }
                                } else {
                                    RoleChip(role.lockRole)
                                }
                            }
                            // Actions
                            if (canManage && !role.email.equals(myEmail, ignoreCase = true) && role.lockRole != "OWNER") {
                                if (editingEmail == role.email) {
                                    IconButton(onClick = { onChangeRole(role.email, editRole); editingEmail = null }) {
                                        Icon(Icons.Default.Check, null, tint = GreenSuccess)
                                    }
                                    IconButton(onClick = { editingEmail = null }) {
                                        Icon(Icons.Default.Close, null, tint = MediumText)
                                    }
                                } else {
                                    IconButton(onClick = { editingEmail = role.email; editRole = role.lockRole }) {
                                        Icon(Icons.Default.Edit, null, tint = MediumText, modifier = Modifier.size(18.dp))
                                    }
                                    IconButton(onClick = { onRemove(role.email) }) {
                                        Icon(Icons.Default.Delete, null, tint = MediumText, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                        }
                        if (i < roles.lastIndex) HorizontalDivider(color = BorderLight)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun KeysTab(
    keys: List<AccessKeyDto>,
    myRole: String,
    onGenerate: (String, String, (String?) -> Unit) -> Unit,
    onRevoke: (String) -> Unit
) {
    val context = LocalContext.current
    var showGenDialog by remember { mutableStateOf(false) }
    var generatedToken by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Generate button
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = { showGenDialog = true },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(Icons.Default.VpnKey, null, Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Генерувати ключ")
            }
        }
        Spacer(Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardWhite),
            elevation = CardDefaults.cardElevation(0.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
        ) {
            if (keys.isEmpty()) {
                Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Немає ключів доступу", color = MediumText)
                }
            } else {
                Column {
                    keys.forEachIndexed { i, key ->
                        Column(Modifier.padding(16.dp)) {
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = key.accessToken?.take(18)?.plus("…") ?: "—",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = FontFamily.Monospace,
                                    color = MediumText,
                                    modifier = Modifier.weight(1f)
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (key.active) GreenBg else SurfaceLight,
                                    contentColor = if (key.active) GreenSuccess else MediumText,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, if (key.active) GreenBorder else BorderGray)
                                ) {
                                    Text(if (key.active) "Активний" else "Відкликано", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                }
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Від: ${key.validFrom?.let { formatDateTime(it) } ?: "—"}", style = MaterialTheme.typography.bodySmall, color = MediumText)
                            Text("До: ${key.validUntil?.let { formatDateTime(it) } ?: "—"}", style = MaterialTheme.typography.bodySmall, color = MediumText)
                            Spacer(Modifier.height(8.dp))
                            OutlinedButton(
                                onClick = { onRevoke(key.accessKeyId) },
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                                border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed),
                                shape = RoundedCornerShape(6.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Delete, null, Modifier.size(14.dp))
                                Spacer(Modifier.width(4.dp))
                                Text("Відкликати", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                        if (i < keys.lastIndex) HorizontalDivider(color = BorderLight)
                    }
                }
            }
        }
    }

    // Generate Key Dialog
    if (showGenDialog) {
        GenerateKeyDialog(
            context = context,
            onDismiss = { showGenDialog = false; generatedToken = null },
            onGenerate = { from, until ->
                onGenerate(from, until) { token ->
                    generatedToken = token
                    if (token == null) showGenDialog = false
                }
            },
            generatedToken = generatedToken,
            onClose = { showGenDialog = false; generatedToken = null }
        )
    }
}

@Composable
private fun GenerateKeyDialog(
    context: Context,
    onDismiss: () -> Unit,
    onGenerate: (String, String) -> Unit,
    generatedToken: String?,
    onClose: () -> Unit
) {
    var validFrom by remember { mutableStateOf("") }
    var validUntil by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    var generating by remember { mutableStateOf(false) }
    var copied by remember { mutableStateOf(false) }

    val sdf = remember { SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()) }
    val displaySdf = remember { SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()) }

    fun pickDateTime(onPicked: (String, String) -> Unit) {
        val now = Calendar.getInstance()
        DatePickerDialog(context, { _, y, m, d ->
            TimePickerDialog(context, { _, h, min ->
                val cal = Calendar.getInstance().apply { set(y, m, d, h, min, 0) }
                onPicked(sdf.format(cal.time), displaySdf.format(cal.time))
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), true).show()
        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardWhite,
        title = { Text("Генерувати ключ доступу", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold) },
        text = {
            if (generatedToken != null) {
                Column {
                    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), color = GreenBg, contentColor = GreenSuccess) {
                        Text("Ключ згенеровано! Збережіть його — він більше не буде відображений.", modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(12.dp))
                    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(8.dp), color = SurfaceLight, border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Text(generatedToken, style = MaterialTheme.typography.bodySmall, fontFamily = FontFamily.Monospace, modifier = Modifier.weight(1f), maxLines = 4, overflow = TextOverflow.Ellipsis)
                            IconButton(onClick = {
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                clipboard.setPrimaryClip(ClipData.newPlainText("access_key", generatedToken))
                                copied = true
                            }) {
                                Icon(if (copied) Icons.Default.Check else Icons.Default.ContentCopy, null, tint = if (copied) GreenSuccess else MediumText)
                            }
                        }
                    }
                }
            } else {
                Column {
                    if (error.isNotEmpty()) {
                        Surface(Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(8.dp), color = ErrorBg, contentColor = ErrorRed) {
                            Text(error, Modifier.padding(10.dp), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                    OutlinedButton(
                        onClick = { pickDateTime { iso, disp -> validFrom = iso } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.CalendarToday, null, Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (validFrom.isEmpty()) "Від: оберіть дату" else "Від: ${validFrom.take(16)}", style = MaterialTheme.typography.bodyMedium)
                    }
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { pickDateTime { iso, disp -> validUntil = iso } },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.CalendarToday, null, Modifier.size(14.dp))
                        Spacer(Modifier.width(6.dp))
                        Text(if (validUntil.isEmpty()) "До: оберіть дату" else "До: ${validUntil.take(16)}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {
            if (generatedToken != null) {
                Button(onClick = onClose, colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text("Готово")
                }
            } else {
                Button(
                    onClick = {
                        if (validFrom.isEmpty() || validUntil.isEmpty()) { error = "Оберіть обидві дати"; return@Button }
                        generating = true; error = ""
                        onGenerate(validFrom, validUntil)
                    },
                    enabled = !generating,
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    if (generating) CircularProgressIndicator(Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                    else Text("Генерувати")
                }
            }
        },
        dismissButton = {
            if (generatedToken == null) {
                OutlinedButton(onClick = onDismiss) { Text("Скасувати") }
            }
        }
    )
}
