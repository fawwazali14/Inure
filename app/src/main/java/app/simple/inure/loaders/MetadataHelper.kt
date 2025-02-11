package app.simple.inure.loaders

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.webkit.MimeTypeMap
import android.webkit.URLUtil
import androidx.documentfile.provider.DocumentFile
import app.simple.inure.R
import app.simple.inure.models.AudioMetaData
import app.simple.inure.util.AudioUtils
import app.simple.inure.util.AudioUtils.toBitrate
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream

object MetadataHelper {

    fun getAudioMetadata(context: Context, songUri: Uri): AudioMetaData {
        val audioMetadata = AudioMetaData()
        val mediaMetadataRetriever = MediaMetadataRetriever()

        if (URLUtil.isValidUrl(songUri.toString()) &&
            (songUri.toString().startsWith("http")
                    || songUri.toString().startsWith("https")
                    || songUri.toString().startsWith("ftp"))) {
            mediaMetadataRetriever.setDataSource(songUri.toString(), hashMapOf())
        } else {
            mediaMetadataRetriever.setDataSource(context, songUri)
        }

        audioMetadata.title = getSongTitleMeta(context, songUri, mediaMetadataRetriever)
        audioMetadata.artists = getSongArtistMeta(context, mediaMetadataRetriever)
        audioMetadata.album = getSongAlbumMeta(context, mediaMetadataRetriever)
        audioMetadata.format = getFileExtension(context, songUri)
        audioMetadata.bitrate = getBitrate(mediaMetadataRetriever)
        audioMetadata.sampling = AudioUtils.getSampling(context, songUri)
        audioMetadata.art = getOriginalAlbumArt(mediaMetadataRetriever)

        mediaMetadataRetriever.close()

        return audioMetadata
    }

    /**
     * Extracts the title metadata from the source
     * audio file
     *
     * @param context
     * @param mediaMetadataRetriever
     * @return [String]
     */
    private fun getSongTitleMeta(context: Context, songUri: Uri, mediaMetadataRetriever: MediaMetadataRetriever): String {
        return try {
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!!
        } catch (e: NullPointerException) {
            DocumentFile.fromSingleUri(context, songUri)?.name ?: context.getString(R.string.unknown)
        }
    }

    /**
     * Extracts the artist metadata from the source
     * audio file
     *
     * @param context
     * @param mediaMetadataRetriever
     * @return [String]
     */
    private fun getSongArtistMeta(context: Context, mediaMetadataRetriever: MediaMetadataRetriever): String {
        return try {
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!!
        } catch (e: NullPointerException) {
            context.getString(R.string.unknown)
        }
    }

    /**
     * Extracts the album metadata from the source
     * audio file
     *
     * @param context
     * @param mediaMetadataRetriever
     * @return [String]
     */
    private fun getSongAlbumMeta(context: Context, mediaMetadataRetriever: MediaMetadataRetriever): String {
        return try {
            mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)!!
        } catch (e: NullPointerException) {
            context.getString(R.string.unknown)
        }
    }

    /**
     * Calculates the bitrate of the given audio file in
     * kbps or mbps and returns the appended string
     *
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws NullPointerException
     * @return Bitrate of the given audio file as String
     */
    private fun getBitrate(mediaMetadataRetriever: MediaMetadataRetriever): String {
        return mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE)!!.toInt().toBitrate()
    }

    /**
     * Get extension of any Mime Type based content URI
     */
    private fun getFileExtension(context: Context, uri: Uri?): String {
        val e = "." + MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri!!))
        return if (e.contains("null")) {
            context.getString(R.string.not_available)
        } else {
            e
        }
    }

    private fun getOriginalAlbumArt(mediaMetadataRetriever: MediaMetadataRetriever): Bitmap? {
        var inputStream: InputStream? = null
        if (mediaMetadataRetriever.embeddedPicture != null) {
            inputStream = ByteArrayInputStream(mediaMetadataRetriever.embeddedPicture)
        }
        return BitmapFactory.decodeStream(inputStream)
    }
}