package com.example.jetweatherapp.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetweatherapp.model.LocationDataItem

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LocationSearchBar(
    modifier: Modifier = Modifier,
    query: String = "",
    onQueryChange: (String) -> Unit = {},
    onSearch: (String) -> Unit = {},
    active: Boolean = false,
    onActiveChange: (Boolean) -> Unit = {},
    onClear: () -> Unit = {},
    searchResult: List<LocationDataItem> = arrayListOf()
) {
    SearchBar(
        modifier = if (active) {
            modifier
                .fillMaxWidth()
                .animateContentSize()
        } else {
            modifier
                .fillMaxWidth()
                .padding(start = 25.dp, end = 25.dp)
                .animateContentSize()
        },
        shadowElevation = 0.dp,
        tonalElevation = 5.dp,
        shape = RoundedCornerShape(15.dp),
        placeholder = {
            Text(text = "Enter Search Location")
        },
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        leadingIcon = {
            Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
        },
        trailingIcon = {
            if (active) {
                Icon(
                    modifier = modifier.clickable { onClear() },
                    imageVector = Icons.Default.Clear,
                    contentDescription = "clear search icon"
                )
            }
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
        ) {
            items(searchResult) {
                Text(text = "${it.name}, ${it.state} ${it.country}")
            }
        }
    }
}