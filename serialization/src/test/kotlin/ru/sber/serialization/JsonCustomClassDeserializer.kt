package ru.sber.serialization

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class JsonCustomClassDeserializer {

    @Test
    fun `Необходимо десериализовать данные в класс`() {
        // given
        val data = """{"client": "Иванов Иван Иванович"}"""
        val objectMapper = ObjectMapper()
            .registerModules(KotlinModule(), SimpleModule().addDeserializer(Client7::class.java,
                    object : JsonDeserializer<Client7>(){
                    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): Client7 {
                        val node : JsonNode = jp.codec.readTree(jp)
                        val (lastName, firstName, middleName) = node["client"].asText().split(" ")
                        return Client7(firstName, lastName, middleName)
                    }
                })
            )

        // when
        val client = objectMapper.readValue<Client7>(data)

        // then
        assertEquals("Иван", client.firstName)
        assertEquals("Иванов", client.lastName)
        assertEquals("Иванович", client.middleName)
    }
}
