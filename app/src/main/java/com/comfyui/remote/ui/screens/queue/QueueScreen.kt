package com.comfyui.remote.ui.screens.queue

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun QueueScreen(
    viewModel: QueueViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "队列",
                style = MaterialTheme.typography.headlineMedium
            )

            Row {
                if (uiState.queueInfo?.queueRunning?.isNotEmpty() == true ||
                    uiState.queueInfo?.queuePending?.isNotEmpty() == true) {
                    IconButton(
                        onClick = viewModel::interrupt,
                        enabled = !uiState.isInterrupting
                    ) {
                        if (uiState.isInterrupting) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Stop,
                                contentDescription = "中断",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }

                IconButton(
                    onClick = viewModel::loadQueue,
                    enabled = !uiState.isLoading
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "刷新"
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading && uiState.queueInfo == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                uiState.queueInfo?.queueRunning?.let { running ->
                    item {
                        Text(
                            text = "正在运行 (${running.size})",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(running) { item ->
                        QueueItemCard(
                            number = item.number,
                            nodeType = item.nodeType,
                            nodeName = item.nodeName
                        )
                    }
                }

                uiState.queueInfo?.queuePending?.let { pending ->
                    if (pending.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "等待中 (${pending.size})",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        items(pending) { item ->
                            QueueItemCard(
                                number = item.number,
                                nodeType = item.nodeType,
                                nodeName = item.nodeName
                            )
                        }
                    }
                }

                if (uiState.queueInfo?.queueRunning?.isNullOrEmpty() == true &&
                    uiState.queueInfo?.queuePending?.isNullOrEmpty() == true) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "队列为空",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun QueueItemCard(
    number: Int,
    nodeType: String,
    nodeName: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "#$number",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = nodeName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = nodeType,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
