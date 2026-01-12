package ru.webrelab.kie.cerealstorage

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class CerealStorageImplTest {

    private val storage = CerealStorageImpl(10f, 20f)

    @Test
    fun `should throw if containerCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(-4f, 10f)
        }
    }

    @Test
    fun `should throw if storageCapacity is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(4f, -10f)
        }
    }

    @Test
    fun `should throw if storageCapacity is less than containerCapacity`() {
        assertThrows(IllegalArgumentException::class.java) {
            CerealStorageImpl(10f, 5f)
        }
    }

    @Test
    fun `addCereal should throw if amount is negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.addCereal(Cereal.RICE, -1f)
        }
    }

    @Test
    fun `addCereal should throw if no space for new container`() {
        storage.addCereal(Cereal.PEAS, 10f)
        storage.addCereal(Cereal.BUCKWHEAT, 10f)
        assertThrows(IllegalStateException::class.java) {
            storage.addCereal(Cereal.MILLET, 1f)
        }
    }

    @Test
    fun `addCereal should return remainder if container full`() {
        val remainder = storage.addCereal(Cereal.BULGUR, 17f)
        assertEquals(7f, remainder, 0.01f)
    }

    @Test
    fun `getCereal should throw if amount negative`() {
        assertThrows(IllegalArgumentException::class.java) {
            storage.getCereal(Cereal.MILLET, -6f)
        }
    }

    @Test
    fun `getCereal should return available amount if requested more`() {
        storage.addCereal(Cereal.BULGUR, 5f)
        val take = storage.getCereal(Cereal.BULGUR, 10f)
        assertEquals(5f, take, 0.01f)
        assertEquals(0f, storage.getAmount(Cereal.BULGUR), 0.01f)
    }

    @Test
    fun `removeContainer should return true if container is empty`() {
        storage.addCereal(Cereal.BUCKWHEAT, 0f)
        assertTrue(storage.removeContainer(Cereal.BUCKWHEAT))
    }

    @Test
    fun `removeContainer should return false if container is not empty`() {
        storage.addCereal(Cereal.RICE, 5f)
        assertFalse(storage.removeContainer(Cereal.RICE))
    }

    @Test
    fun `removeContainer after adding and then taking all cereal`() {
        storage.addCereal(Cereal.RICE, 5f)
        val take = storage.getCereal(Cereal.RICE, 6f)
        assertTrue(storage.removeContainer(Cereal.RICE))
    }

    @Test
    fun `getAmount should return 0 if no container`() {
        assertEquals(0f, storage.getAmount(Cereal.MILLET), 0.01f)
    }

    @Test
    fun `getSpace should return space`() {
        storage.addCereal(Cereal.PEAS, 7f)
        assertEquals(3f, storage.getSpace(Cereal.PEAS), 0.01f)
    }

    @Test
    fun `getSpace should throw if not container`() {
        assertThrows(IllegalStateException::class.java) {
            storage.getSpace(Cereal.BULGUR)
        }
    }

    @Test
    fun `toString should return no empty string`() {
        storage.addCereal(Cereal.BUCKWHEAT, 5f)
        assertTrue(storage.toString().contains("BUCKWHEAT"))
    }

}