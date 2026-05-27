package com.hnure.smartlock.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hnure.smartlock.data.api.LockDto
import com.hnure.smartlock.ui.components.ConfirmDialog
import com.hnure.smartlock.ui.components.LockCard
import com.hnure.smartlock.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToLockDetail: (String) -> Unit,
    onNavigateToProfile: () -> Unit,
    onLogout: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showCreateSheet by remember { mutableStateOf(false) }
    var lockToDelete by remember { mutableStateOf<LockDto?>(null) }

    val pullRefreshState = rememberPullToRefreshState()
    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            viewModel.fetchLocks()
            pullRefreshState.endRefresh()
        }
    }

    // Error snackbar
    val snackbarHostState = remember { SnackbarHostState() }
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(30.dp)
                                .clip(RoundedCornerShape(7.dp))
                                .background(PrimaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Lock, null, tint = Color.White, modifier = Modifier.size(16.dp))
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            "SmartLock",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.AccountCircle, "Профіль", tint = MediumText)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CardWhite,
                    scrolledContainerColor = CardWhite
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateSheet = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, "Додати замок")
            }
        },
        containerColor = BackgroundGray
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                item {
                    Column {
                        Text(
                            text = "Мої замки",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            text = "${state.totalCount} ${if (state.totalCount == 1) "пристрій" else "пристроїв"}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MediumText
                        )
                        Spacer(Modifier.height(12.dp))

                        // Search
                        OutlinedTextField(
                            value = state.searchQuery,
                            onValueChange = viewModel::onSearchChange,
                            placeholder = { Text("Пошук замків...", color = MediumText) },
                            leadingIcon = { Icon(Icons.Default.Search, null, tint = MediumText) },
                            trailingIcon = {
                                if (state.searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.onSearchChange("") }) {
                                        Icon(Icons.Default.Clear, null, tint = MediumText)
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryBlue,
                                unfocusedBorderColor = BorderGray,
                                unfocusedContainerColor = CardWhite,
                                focusedContainerColor = CardWhite
                            )
                        )
                        Spacer(Modifier.height(4.dp))
                    }
                }

                // Stats row
                if (state.locks.isNotEmpty()) {
                    item {
                        StatsRow(state = state)
                        Spacer(Modifier.height(4.dp))
                    }
                }

                // Loading skeletons
                if (state.isLoading) {
                    items(4) {
                        ShimmerLockCard()
                    }
                } else if (state.filtered.isEmpty()) {
                    // Empty state
                    item {
                        EmptyState(
                            isSearch = state.searchQuery.isNotEmpty(),
                            searchQuery = state.searchQuery,
                            onAddClick = { showCreateSheet = true }
                        )
                    }
                } else {
                    items(state.filtered, key = { it.lockId }) { lock ->
                        LockCard(
                            lock = lock,
                            onToggle = { lockId, isLocked -> viewModel.toggleLock(lockId, isLocked) },
                            onDelete = { lockToDelete = it },
                            onOpenDetail = onNavigateToLockDetail
                        )
                    }
                }

                item { Spacer(Modifier.height(80.dp)) }
            }

            PullToRefreshContainer(
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                containerColor = CardWhite,
                contentColor = PrimaryBlue
            )
        }
    }

    // Create Lock BottomSheet
    if (showCreateSheet) {
        CreateLockBottomSheet(
            onDismiss = { showCreateSheet = false },
            onCreate = { name, serial, tz ->
                viewModel.createLock(name, serial, tz) { showCreateSheet = false }
            }
        )
    }

    // Delete confirm dialog
    lockToDelete?.let { lock ->
        ConfirmDialog(
            title = "Видалити замок",
            message = "Видалити замок \"${lock.name}\"? Цю дію не можна скасувати.",
            confirmLabel = "Видалити",
            onConfirm = {
                viewModel.deleteLock(lock.lockId)
                lockToDelete = null
            },
            onDismiss = { lockToDelete = null }
        )
    }
}

@Composable
private fun StatsRow(state: HomeUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StatChip("Всього", state.totalCount.toString(), MediumText, Modifier.weight(1f))
        StatChip("Замкнуто", state.lockedCount.toString(), PrimaryBlue, Modifier.weight(1f))
        StatChip("Онлайн", state.onlineCount.toString(), GreenSuccess, Modifier.weight(1f))
    }
}

@Composable
private fun StatChip(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        color = CardWhite,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = valueColor)
            Text(label, style = MaterialTheme.typography.bodySmall, color = MediumText)
        }
    }
}

@Composable
private fun EmptyState(isSearch: Boolean, searchQuery: String, onAddClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(LightBlue),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Lock, null, tint = PrimaryBlue, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.height(16.dp))
        Text(
            text = if (isSearch) "Нічого не знайдено" else "Немає замків",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (isSearch) "За запитом \"$searchQuery\" нічого не знайдено"
                   else "Натисніть + щоб додати перший замок",
            style = MaterialTheme.typography.bodyMedium,
            color = MediumText
        )
        if (!isSearch) {
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = onAddClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(6.dp))
                Text("Додати замок")
            }
        }
    }
}

@Composable
private fun ShimmerLockCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        elevation = CardDefaults.cardElevation(0.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderGray)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Box(Modifier.size(44.dp).clip(RoundedCornerShape(8.dp)).background(BorderGray))
                Spacer(Modifier.width(12.dp))
                Column {
                    Box(Modifier.height(16.dp).fillMaxWidth(0.5f).clip(RoundedCornerShape(4.dp)).background(BorderGray))
                    Spacer(Modifier.height(6.dp))
                    Box(Modifier.height(12.dp).fillMaxWidth(0.3f).clip(RoundedCornerShape(4.dp)).background(BorderLight))
                }
            }
            Spacer(Modifier.height(12.dp))
            Box(Modifier.height(12.dp).fillMaxWidth(0.7f).clip(RoundedCornerShape(4.dp)).background(BorderGray))
            Spacer(Modifier.height(6.dp))
            Box(Modifier.height(10.dp).fillMaxWidth(0.4f).clip(RoundedCornerShape(4.dp)).background(BorderLight))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateLockBottomSheet(
    onDismiss: () -> Unit,
    onCreate: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var timezone by remember { mutableStateOf("Europe/Kyiv") }
    var error by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CardWhite,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 40.dp)) {
            Text("Додати замок", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(4.dp))
            Text("Заповніть дані нового замку", style = MaterialTheme.typography.bodyMedium, color = MediumText)
            Spacer(Modifier.height(20.dp))

            AnimatedVisibility(visible = error.isNotEmpty()) {
                Surface(Modifier.fillMaxWidth().padding(bottom = 12.dp), shape = RoundedCornerShape(8.dp), color = ErrorBg, contentColor = ErrorRed) {
                    Text(error, modifier = Modifier.padding(12.dp), style = MaterialTheme.typography.bodyMedium)
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it; error = "" },
                label = { Text("Назва замку") },
                placeholder = { Text("напр. Передня двері", color = MediumText) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderGray)
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = serial,
                onValueChange = { serial = it; error = "" },
                label = { Text("Серійний номер") },
                placeholder = { Text("SN-XXXX-XXXX", color = MediumText) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderGray)
            )
            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = timezone,
                onValueChange = { timezone = it },
                label = { Text("Часовий пояс") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryBlue, unfocusedBorderColor = BorderGray)
            )
            Spacer(Modifier.height(20.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp)
                ) { Text("Скасувати") }

                Button(
                    onClick = {
                        if (name.isBlank() || serial.isBlank()) { error = "Заповніть назву та серійний номер"; return@Button }
                        loading = true
                        onCreate(name.trim(), serial.trim(), timezone.trim().ifEmpty { "UTC" })
                    },
                    enabled = !loading,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) {
                    if (loading) CircularProgressIndicator(Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                    else Text("Додати", fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
