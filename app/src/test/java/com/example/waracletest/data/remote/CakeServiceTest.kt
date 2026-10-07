package com.example.waracletest.data.remote

import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class CakeServiceTest {
    private lateinit var server: MockWebServer

    @Before
    fun setUp() {
        server = MockWebServer()
        server.start()
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `loadCakes requests cakes endpoint and deserializes response`() =
        runBlocking {
            server.enqueue(
                MockResponse()
                    .setHeader("Content-Type", "application/json")
                    .setBody(
                        """
                        [
                          {
                            "title": "Dundee Cake",
                            "desc": "A staple with Dundonians",
                            "image": "https://example.com/dundee.jpg"
                          }
                        ]
                        """.trimIndent(),
                    ),
            )

            val cakes = CakeServiceFactory.create(server.url("/").toString()).loadCakes()

            assertEquals("/cakes", server.takeRequest().path)
            assertEquals(
                CakeDto(
                    title = "Dundee Cake",
                    desc = "A staple with Dundonians",
                    image = "https://example.com/dundee.jpg",
                ),
                cakes.single(),
            )
        }
}
