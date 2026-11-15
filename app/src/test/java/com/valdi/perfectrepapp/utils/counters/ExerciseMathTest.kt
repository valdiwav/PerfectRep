package com.valdi.perfectrepapp.utils.counters

import org.junit.Assert.assertEquals
import org.junit.Test

class ExerciseMathTest {

    private val delta = 1e-6

    @Test
    fun `pierna extendida forma un angulo de 180 grados`() {
        val hip = 0f to 0f
        val knee = 0f to 10f
        val ankle = 0f to 20f
        assertEquals(180.0, ExerciseMath.angleAt(hip, knee, ankle), delta)
    }

    @Test
    fun `sentadilla paralela forma un angulo recto en la rodilla`() {
        val hip = 0f to 0f
        val knee = 10f to 0f
        val ankle = 10f to 10f
        assertEquals(90.0, ExerciseMath.angleAt(hip, knee, ankle), delta)
    }

    @Test
    fun `el angulo nunca supera 180 grados y no depende del orden de los extremos`() {
        val a = 10f to 0f
        val vertex = 0f to 0f
        val b = -10f to -1f
        val angle = ExerciseMath.angleAt(a, vertex, b)
        assert(angle in 0.0..180.0)
        assertEquals(angle, ExerciseMath.angleAt(b, vertex, a), delta)
    }

    @Test
    fun `sin calibrar se asume RIR maximo`() {
        assertEquals(ExerciseMath.MAX_RIR, ExerciseMath.estimateRir(0.5, null))
    }

    @Test
    fun `el RIR baja a medida que cae la velocidad`() {
        val base = 1.0
        assertEquals(4, ExerciseMath.estimateRir(1.1, base))
        assertEquals(4, ExerciseMath.estimateRir(1.0, base))
        assertEquals(3, ExerciseMath.estimateRir(0.95, base))
        assertEquals(2, ExerciseMath.estimateRir(0.85, base))
        assertEquals(1, ExerciseMath.estimateRir(0.75, base))
        assertEquals(0, ExerciseMath.estimateRir(0.6, base))
    }
}
