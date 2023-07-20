package com.dragos.domain

import com.dragos.domain.util.UrlUtils
import org.junit.Assert
import org.junit.Test

class UrlUtilsUnitTest {
    @Test
    fun `ID extracted correctly when the path ends with a single slash`() {
        val url = "https://example.com/25/"
        Assert.assertEquals(25, UrlUtils.getIdFromUrl(url))
    }

    @Test
    fun `ID extracted correctly when the path ends with multiple slashes`() {
        val url = "https://example.com/25///"
        Assert.assertEquals(25, UrlUtils.getIdFromUrl(url))
    }

    @Test
    fun `ID extracted correctly when the path ends with no slash`() {
        val url = "https://example.com/25"
        Assert.assertEquals(25, UrlUtils.getIdFromUrl(url))
    }

    @Test
    fun `ID extracted correctly when the path ends with query params`() {
        val url = "https://example.com/25?param=52"
        Assert.assertEquals(25, UrlUtils.getIdFromUrl(url))
    }
    @Test
    fun `ID extracted correctly when the path has multiple segments`() {
        val url = "https://example.com/test/12/25?param=52"
        Assert.assertEquals(25, UrlUtils.getIdFromUrl(url))
    }

    @Test
    fun `ID cannot be extracted for a malformed url`() {
        val url = "i'm not an url"
        Assert.assertNull(UrlUtils.getIdFromUrl(url))
    }
}