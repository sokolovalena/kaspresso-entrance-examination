package ru.webrelab.kie.cerealstorage

class CerealStorageImpl(
    override val containerCapacity: Float,
    override val storageCapacity: Float
) : CerealStorage {

    /**
     * Блок инициализации класса.
     * Выполняется сразу при создании объекта
     */
    init {
        require(containerCapacity >= 0) {
            "Ёмкость контейнера не может быть отрицательной"
        }
        require(storageCapacity >= containerCapacity) {
            "Ёмкость хранилища не должна быть меньше ёмкости одного контейнера"
        }
    }

    private val storage = mutableMapOf<Cereal, Float>()

    override fun addCereal(cereal: Cereal, amount: Float): Float {
        if (amount < 0) {
            throw IllegalArgumentException("Передано отрицательное значение")
        }

        // Если такой крупы еще нет, попытаемся добавить контейнер
        if (!storage.containsKey(cereal)) {
            if ((storage.size + 1) * containerCapacity > storageCapacity) {
                throw IllegalStateException("В хранилище нет места для еще одного контейнера")
            }
            storage[cereal] = 0f
        }

        val current = storage[cereal] ?: 0f
        val space = containerCapacity - current

        if (amount > space) {
            storage[cereal] = containerCapacity
            return amount - space
        } else {
            storage[cereal] = current + amount
            return 0f
        }
    }

    override fun getCereal(cereal: Cereal, amount: Float): Float {
        if (amount < 0f) {
            throw IllegalArgumentException("Передано отрицательное значение")
        }

        val current = storage[cereal] ?: 0f

        if (amount > current) {
            storage[cereal] = 0f
            return current
        } else {
            storage[cereal] = current - amount
            return amount
        }
    }

    override fun removeContainer(cereal: Cereal): Boolean {
        if (!storage.containsKey(cereal)) {
            return false
        }

        val current = storage[cereal] ?: 0f

        if (current == 0.0f) {
            storage.remove(cereal)
            return true
        }

        return false
    }

    override fun getAmount(cereal: Cereal): Float {
        if (!storage.containsKey(cereal)) {
            return 0f
        }
        return storage[cereal] ?: 0f
    }

    override fun getSpace(cereal: Cereal): Float {
        if (!storage.containsKey(cereal)) {
            throw IllegalStateException("Контейнера этой крупы нет")
        }

        val current = storage[cereal] ?: 0f
        val space = containerCapacity - current
        return space
    }

    override fun toString(): String {
        var res = "Хранилище:"
        for (entry in storage) {
            res += "\n${entry.key}: ${entry.value}"
        }
        return res
    }

}
