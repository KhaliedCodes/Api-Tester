package com.instabug.android_challenge.utils

import android.net.Uri
import android.os.Environment
import android.provider.OpenableColumns
import com.instabug.android_challenge.Utils.FilesUtils
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.mockito.Mockito
import java.io.ByteArrayInputStream
import java.io.File

class FileUtilsTest {

    @Rule
    @JvmField
    val temporaryFolder: TemporaryFolder = TemporaryFolder()


    private lateinit var testOutputFolder: File

    @Before
    fun setup(){
        testOutputFolder = temporaryFolder.newFolder("test")
        if (!testOutputFolder.exists()) {
            testOutputFolder.mkdirs()
        }
    }




    @Test
    fun `convertInputStreamToFile returns null when input stream is null`() {
        val result = FilesUtils.convertInputStreamToFile(null, "testFile.txt", testOutputFolder)
        Assert.assertEquals(null, result)
    }


    @Test
    fun `convertInputStreamToFile returns valid File for a non-null input stream`() {
        val inputContent = "Test content"
        val inputStream = ByteArrayInputStream(inputContent.toByteArray())
        val result = FilesUtils.convertInputStreamToFile(inputStream, "testFile.txt", testOutputFolder)

        Assert.assertEquals("testFile.txt", result?.name)
    }


    @Test
    fun createFolder() {
        val testFolder = File(temporaryFolder.newFolder("folder").absolutePath + "/testFolder")
        val created = FilesUtils.createFolder(testFolder)
        Assert.assertTrue(created)
        Assert.assertTrue(testFolder.exists())
    }


    @Test
    fun `createFolder returns false when folder already exists`() {
        val testFolder = temporaryFolder.newFolder("folder")
        val created = FilesUtils.createFolder(testFolder)
        Assert.assertFalse(created)
        Assert.assertTrue(testFolder.exists())
    }


}