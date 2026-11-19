package com.healthmate.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// ============================================================
// HealthMate Shape System
// Consistent corner radii across the application
// ============================================================

val Shapes = Shapes(
    // Small components: chips, small buttons
    small = RoundedCornerShape(8.dp),

    // Medium components: cards, text fields
    medium = RoundedCornerShape(12.dp),

    // Large components: modal sheets, prominent cards
    large = RoundedCornerShape(16.dp),

    // Extra large: bottom sheets, large containers
    extraLarge = RoundedCornerShape(24.dp)
)