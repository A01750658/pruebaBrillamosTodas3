package mx.tec.pruebabrillamostodas3.model

import android.content.Context
import java.io.File
import java.io.FileOutputStream
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class AESEncryption {

    fun generateAESKey(keySize: Int = 256): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(keySize)
        return keyGenerator.generateKey()
    }
    fun aesEncrypt(data: ByteArray, secretKey: SecretKey): ByteArray {
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        val ivParameterSpec = IvParameterSpec(ByteArray(16)) // Use a secure IV in production
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec)
        return cipher.doFinal(data)
    }


    fun saveKeyToInternalStorage(context: Context, secretKey: SecretKey, fileName: String) {
        val keyBytes = secretKey.encoded
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { fos ->
            fos.write(keyBytes)
        }
    }

    fun readKeyFromInternalStorage(context: Context, fileName: String): SecretKey {
        val file = File(context.filesDir, fileName)
        val keyBytes = file.readBytes()
        return SecretKeySpec(keyBytes, "AES")
    }

}