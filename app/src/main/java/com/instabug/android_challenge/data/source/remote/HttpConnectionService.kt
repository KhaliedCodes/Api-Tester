package com.instabug.android_challenge.data.source.remote

import android.util.Log
import com.instabug.android_challenge.model.Response
import com.instabug.android_challenge.model.enums.RequestMethodEnum
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import kotlin.time.measureTime


class HttpConnectionService {
    private val boundary = "*****"
    private val crlf = "\r\n"
    private val twoHyphens = "--"

    fun sendGetRequest(url: String, headers: Map<String, String> = emptyMap()): Response {
        val urlGetRequest = URL(url)
        val httpURLConnection = urlGetRequest.openConnection() as HttpURLConnection



        headers.entries.forEach {
            httpURLConnection.setRequestProperty(it.key, it.value)
            Log.e(it.key, it.value)
        }


        httpURLConnection.requestMethod = RequestMethodEnum.GET.name
        Log.e("method", RequestMethodEnum.GET.name)

        val requestHeaders = httpURLConnection.requestProperties

        try {
            // Response code
            val duration = measureTime {
                httpURLConnection.responseCode
            }
            val responseCode = httpURLConnection.responseCode
            if (responseCode in 200..299) {
                // Read the response
                val `in` = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val response = StringBuffer()
                var inputLine: String? = null

                while ((`in`.readLine().also { inputLine = it }) != null) {
                    response.append(inputLine)
                }
                `in`.close()

                // Return response
                return Response(url, response.toString(), null, null, responseCode, httpURLConnection.headerFields, requestHeaders, duration.inWholeMilliseconds)
            } else {
                val `in` = BufferedReader(InputStreamReader(httpURLConnection.errorStream))
                val response = StringBuffer()
                var inputLine: String?

                while ((`in`.readLine().also { inputLine = it }) != null) {
                    response.append(inputLine)
                }
                `in`.close()
                return Response(url, null, null, response.toString(), responseCode, httpURLConnection.headerFields, requestHeaders, duration.inWholeMilliseconds)
            }
        } catch (e: Exception){
            e.printStackTrace()
            return Response(url, null, null, e.message, httpURLConnection.responseCode, null, requestHeaders, 0)
        } finally {
            httpURLConnection.disconnect()
        }
    }

    fun sendPostRequest(url: String, body: String = "", headers: Map<String, String> = emptyMap()): Response {
        val urlGetRequest = URL(url)
        val httpURLConnection = urlGetRequest.openConnection() as HttpURLConnection


        Log.e("Request body", body)
        headers.entries.forEach {
            httpURLConnection.setRequestProperty(it.key, it.value)
            Log.e(it.key, it.value)
        }

        httpURLConnection.setRequestProperty("Content-Type", "application/json")
        httpURLConnection.setRequestProperty("Accept", "application/json")
        val requestHeaders = httpURLConnection.requestProperties
        httpURLConnection.doOutput = true;
        val outputStream = httpURLConnection.outputStream
        val outputStreamWriter = OutputStreamWriter(outputStream, "UTF-8")
        outputStreamWriter.write(body)
        outputStreamWriter.flush()
        outputStreamWriter.close()
        outputStream.close()


        httpURLConnection.requestMethod = RequestMethodEnum.POST.name
        Log.e("method", RequestMethodEnum.POST.name)
        try {
            // Response code
            val duration = measureTime {
                httpURLConnection.responseCode
            }
            val responseCode = httpURLConnection.responseCode

            if (responseCode in 200..299) {
                // Read the response
                val `in` = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val response = StringBuffer()
                var inputLine: String? = null

                while ((`in`.readLine().also { inputLine = it }) != null) {
                    response.append(inputLine)
                }
                `in`.close()

                // Return response
                return Response(url, response.toString(), body, null,  responseCode, httpURLConnection.headerFields, requestHeaders, duration.inWholeMilliseconds)
            } else {
                val `in` = BufferedReader(InputStreamReader(httpURLConnection.errorStream))
                val response = StringBuffer()
                var inputLine: String?

                while ((`in`.readLine().also { inputLine = it }) != null) {
                    response.append(inputLine)
                }
                `in`.close()
                return Response(url, null, body, response.toString(), responseCode, httpURLConnection.headerFields, requestHeaders, duration.inWholeMilliseconds)
            }
        } catch (e: Exception){
            e.printStackTrace()
            return Response(url, null, body, e.message, httpURLConnection.responseCode, null, requestHeaders, 0)
        } finally {
            httpURLConnection.disconnect()
        }
    }

    fun sendPostRequestMultiPart(url: String, file: File?, headers: Map<String, String> = emptyMap()): Response {
        val urlGetRequest = URL(url)
        val httpURLConnection = urlGetRequest.openConnection() as HttpURLConnection


        headers.entries.forEach {
            httpURLConnection.setRequestProperty(it.key, it.value)
            Log.e(it.key, it.value)
        }

        httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + this.boundary)
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");

        val requestHeaders = httpURLConnection.requestProperties
        httpURLConnection.doOutput = true;
        val outputStream = httpURLConnection.outputStream
        val dataOutputStream = DataOutputStream(outputStream)
//        addFormField("content", "fileUploaded", dataOutputStream)
        addFilePart("file", file, dataOutputStream)
        dataOutputStream.writeBytes(this.crlf)
        dataOutputStream.writeBytes(this.twoHyphens + this.boundary + this.twoHyphens+ this.crlf)
        dataOutputStream.flush()
        dataOutputStream.close()
        outputStream.close()



        httpURLConnection.requestMethod = RequestMethodEnum.POST.name
        Log.e("method", RequestMethodEnum.POST.name)
        try {
            // Response code
            val duration = measureTime {
                httpURLConnection.responseCode
            }
            val responseCode = httpURLConnection.responseCode

            if (responseCode in 200..299) {
                // Read the response
                val `in` = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                val response = StringBuffer()
                var inputLine: String? = null

                while ((`in`.readLine().also { inputLine = it }) != null) {
                    response.append(inputLine)
                }
                `in`.close()

                // Return response
                return Response(url, response.toString(), file.toString(), null, responseCode, httpURLConnection.headerFields, requestHeaders,  duration.inWholeMilliseconds)
            } else {
                val `in` = BufferedReader(InputStreamReader(httpURLConnection.errorStream))
                val response = StringBuffer()
                var inputLine: String?

                while ((`in`.readLine().also { inputLine = it }) != null) {
                    response.append(inputLine)
                }
                `in`.close()
                return Response(url, null, file.toString(), response.toString(), responseCode, httpURLConnection.headerFields, requestHeaders,  duration.inWholeMilliseconds)
            }
        } catch (e: Exception){
            e.printStackTrace()
            return Response(url, null, file.toString(), e.message, httpURLConnection.responseCode, null, requestHeaders,  0)
        } finally {
            httpURLConnection.disconnect()
        }
    }


    @Throws(IOException::class)
    fun addFormField(name: String, value: String, dataOutputStream: DataOutputStream) {
        dataOutputStream.writeBytes(this.twoHyphens + this.boundary + this.crlf)
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + name + "\"" + this.crlf)
        dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + this.crlf)
        dataOutputStream.writeBytes(this.crlf)
        dataOutputStream.writeBytes(value + this.crlf)
        dataOutputStream.flush()
    }


    @Throws(IOException::class)
    fun addFilePart(fieldName: String, uploadFile: File?, dataOutputStream: DataOutputStream) {
        uploadFile?.let {
            val fileName = uploadFile.name
            dataOutputStream.writeBytes(this.twoHyphens + this.boundary + this.crlf)
            dataOutputStream.writeBytes(
                "Content-Disposition: form-data; name=\"" +
                        fieldName + "\";filename=\"" +
                        fileName + "\"" + this.crlf
            )
            dataOutputStream.writeBytes(this.crlf)

            val bytes: ByteArray = Files.readAllBytes(uploadFile.toPath())
            dataOutputStream.write(bytes)
        }

    }
}