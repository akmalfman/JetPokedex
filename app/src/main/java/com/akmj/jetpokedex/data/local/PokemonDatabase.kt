package com.akmj.jetpokedex.data.local

import android.content.Context
import com.akmj.jetpokedex.domain.model.AbilitiesItem
import com.akmj.jetpokedex.domain.model.Ability
import com.akmj.jetpokedex.domain.model.ResultsItem
import com.couchbase.lite.*

class PokemonDatabase(context: Context) {

    private val database = Database("pokemon", DatabaseConfiguration())

    /** 🔹 Simpan list Pokemon ke local database */
    fun savePokemonList(pokemonList: List<ResultsItem>) {
        try {
            pokemonList.forEach { pokemon ->
                pokemon.name?.let { name ->
                    val doc = MutableDocument("pokemon_$name")
                        .setString("name", name)
                        .setString("url", pokemon.url)
                    database.save(doc)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /** 🔹 Ambil list Pokemon dari local database */
    fun getPokemonList(limit: Int = 10, offset: Int = 0): List<ResultsItem> {
        val query = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.database(database))
            .limit(Expression.intValue(limit), Expression.intValue(offset))

        val result = query.execute().allResults()
        return result.mapNotNull {
            val data = it.getDictionary("pokemon")
            if (data != null) {
                ResultsItem(
                    name = data.getString("name"),
                    url = data.getString("url")
                )
            } else null
        }
    }

    /** 🔹 Cari Pokemon berdasarkan nama */
    fun searchPokemon(query: String): List<ResultsItem> {
        val searchQuery = QueryBuilder
            .select(SelectResult.all())
            .from(DataSource.database(database))
            .where(
                Expression.property("name")
                    .like(Expression.string("%${query.lowercase()}%"))
            )

        val result = searchQuery.execute().allResults()
        return result.mapNotNull {
            val data = it.getDictionary("pokemon")
            if (data != null) {
                ResultsItem(
                    name = data.getString("name"),
                    url = data.getString("url")
                )
            } else null
        }
    }

    /** 🔹 Simpan detail Pokemon (abilities) */
    fun savePokemonDetail(name: String, abilities: List<AbilitiesItem>) {
        val doc = MutableDocument("detail_$name")
        val abilitiesArray = MutableArray()

        abilities.forEach { ability ->
            val abilityDict = MutableDictionary()
            abilityDict.setBoolean("is_hidden", ability.isHidden ?: false)
            abilityDict.setInt("slot", ability.slot ?: 0)

            val abilityInfo = MutableDictionary()
            abilityInfo.setString("name", ability.ability?.name)
            abilityInfo.setString("url", ability.ability?.url)
            abilityDict.setDictionary("ability", abilityInfo)

            abilitiesArray.addDictionary(abilityDict)
        }

        doc.setString("name", name)
        doc.setArray("abilities", abilitiesArray)
        database.save(doc)
    }

    /** 🔹 Ambil detail Pokemon (abilities) dari local */
    fun getPokemonDetail(name: String): List<AbilitiesItem>? {
        val doc = database.getDocument("detail_$name") ?: return null

        val abilitiesArray = doc.getArray("abilities") ?: return null
        val abilities = mutableListOf<AbilitiesItem>()

        for (i in 0 until abilitiesArray.count()) {
            val abilityDict = abilitiesArray.getDictionary(i)
            val abilityInfo = abilityDict?.getDictionary("ability")

            abilities.add(
                AbilitiesItem(
                    isHidden = abilityDict?.getBoolean("is_hidden"),
                    slot = abilityDict?.getInt("slot"),
                    ability = Ability(
                        name = abilityInfo?.getString("name"),
                        url = abilityInfo?.getString("url")
                    )
                )
            )
        }

        return abilities
    }

    /** 🔹 Cek apakah ada data Pokemon di local */
    fun hasPokemonData(): Boolean {
        try {
            val query = QueryBuilder
                .select(SelectResult.all())
                .from(DataSource.database(database))
                .limit(Expression.intValue(1))

            val results = query.execute().allResults()
            return results.isNotEmpty()
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    /** 🔹 Hapus semua data Pokemon (untuk refresh) */
    fun clearAllPokemon() {
        try {
            val query = QueryBuilder
                .select(SelectResult.expression(Meta.id))
                .from(DataSource.database(database))

            query.execute().forEach { result ->
                val docId = result.getString(0)
                docId?.let {
                    database.getDocument(it)?.let { doc ->
                        database.delete(doc)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}