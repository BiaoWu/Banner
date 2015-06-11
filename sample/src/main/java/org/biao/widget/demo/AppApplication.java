package org.biao.widget.demo;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class AppApplication extends Application {
    @Override
    public void onCreate() {
        initImageLoadCofig(getApplicationContext());
    }

    /**
     * @方法名： initImageLoadCofig<br>
     * @功能说明：图片异步加载配置项<br>
     */
    private void initImageLoadCofig(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY)
                .denyCacheImageMultipleSizesInMemory()
                        // .diskCacheFileNameGenerator(GlobalParams.md5FileNameGenerator)
                .threadPoolSize(5).memoryCacheSize(2 * 1024 * 1024)
                .diskCache(new UnlimitedDiscCache(
                        StorageUtils.getCacheDirectory(getApplicationContext())))
                        // .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }

}
