package com.valdi.perfectrepapp.utils.counters

import kotlin.math.abs
import kotlin.math.atan2

/**
 * Cálculos geométricos y de esfuerzo compartidos por los contadores de ejercicios.
 *
 * Es código Kotlin puro (sin dependencias de Android), por lo que se puede probar
 * con tests unitarios en la JVM.
 */
object ExerciseMath {

    /** RIR que se asume mientras aún no hay una velocidad base calibrada. */
    const val MAX_RIR = 4

    /**
     * Ángulo, en grados (0–180), que forman los segmentos [p1]–[vertex] y [p3]–[vertex].
     * Por ejemplo, para la rodilla: cadera, rodilla, tobillo.
     */
    fun angleAt(p1: Pair<Float, Float>, vertex: Pair<Float, Float>, p3: Pair<Float, Float>): Double {
        val angle = Math.toDegrees(
            atan2((p3.second - vertex.second).toDouble(), (p3.first - vertex.first).toDouble()) -
                    atan2((p1.second - vertex.second).toDouble(), (p1.first - vertex.first).toDouble())
        )
        return abs(angle).let { if (it > 180) 360 - it else it }
    }

    /**
     * Estima las repeticiones en reserva (RIR) según cuánto ha caído la velocidad de la
     * repetición actual respecto a la velocidad base de las repeticiones de calibración.
     *
     * - Sin caída (≥ 100 %) → 4
     * - Caída < 10 %        → 3
     * - Caída 10–20 %       → 2
     * - Caída 20–30 %       → 1
     * - Caída > 30 %        → 0 (fallo muscular cercano)
     */
    fun estimateRir(currentSpeed: Double, baseSpeed: Double?): Int {
        val base = baseSpeed ?: return MAX_RIR
        val speedRatio = currentSpeed / base
        return when {
            speedRatio >= 1 -> 4
            speedRatio > 0.9 -> 3
            speedRatio > 0.8 -> 2
            speedRatio > 0.7 -> 1
            else -> 0
        }
    }
}
