package com.eaglesakura.io;

import com.eaglesakura.collection.DataCollection;
import com.eaglesakura.lambda.CallbackUtils;
import com.eaglesakura.lambda.CancelCallback;
import com.eaglesakura.util.CollectionUtil;
import com.eaglesakura.util.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Java標準機能でZIPファイルの解凍を行う
 *
 * Androidでは標準IOの仕様上、日本語ファイル名は文字化けする。
 */
public class ZipDecoder {
    InputStream mInputStream;

    /**
     * 出力ディレクトリ
     */
    File mOutputDir;

    public ZipDecoder(InputStream is) {
        mInputStream = is;
        mOutputDir = new File("./");
    }

    /**
     * 出力先ディレクトリを設定する
     */
    public void setOutputDir(File outputDir) {
        mOutputDir = outputDir;
    }

    /**
     * zip解答を行う
     *
     * @param callback       デコードコールバック
     * @param cancelCallback キャンセルチェック
     */
    public DataCollection<File> unzip(Callback callback, CancelCallback cancelCallback) throws IOException {
        ZipInputStream is = new ZipInputStream(new CancelableInputStream(mInputStream, cancelCallback));
        byte[] buffer = new byte[1024 * 128];

        List<File> result = new ArrayList<>();

        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            File outFile = mOutputDir;
            List<String> path = CollectionUtil.asList(entry.getName().split("/"));

            // "/"で区切られていたら、パスを追加する
            while (path.size() > 1) {
                outFile = new File(outFile, path.remove(0));
            }

            // パスを生成する
            outFile.mkdirs();

            // ファイル名を確定する
            outFile = new File(outFile, path.get(0));
            if (!entry.isDirectory() && callback.isDecompressExist(entry.getSize(), outFile)) {
                // ファイルへ書き込む
                FileOutputStream os = null;
                try {
                    os = new FileOutputStream(outFile);
                    int read;
                    while ((read = is.read(buffer)) > 0) {
                        os.write(buffer, 0, read);
                        if (CallbackUtils.isCanceled(cancelCallback)) {
                            throw new InterruptedIOException();
                        }
                    }

                    os.flush();
                    result.add(outFile);
                } finally {
                    IOUtil.close(os);
                }

                callback.onDecompressCompleted(outFile);
            }
        }

        return new DataCollection<>(result);
    }

    public interface Callback {
        /**
         * 指定したファイルを書き込むことを許可する
         */

        boolean isDecompressExist(long size, File dst);

        /**
         * ファイルの解析が完了した
         */
        void onDecompressCompleted(File dst);
    }
}
