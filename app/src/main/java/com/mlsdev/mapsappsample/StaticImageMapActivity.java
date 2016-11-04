package com.mlsdev.mapsappsample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mlsdev.mapsappsample.databinding.ActivityStaticImageMapBinding;
import com.mlsdev.mapsappsample.url.UrlBuilder;

import java.util.Map;

import static com.mlsdev.mapsappsample.url.Constants.CENTER;
import static com.mlsdev.mapsappsample.url.Constants.KEY;
import static com.mlsdev.mapsappsample.url.Constants.MAP_TYPE;
import static com.mlsdev.mapsappsample.url.Constants.SCALE;
import static com.mlsdev.mapsappsample.url.Constants.SIZE;
import static com.mlsdev.mapsappsample.url.Constants.ZOOM;


public class StaticImageMapActivity extends BaseActivity {
    private ActivityStaticImageMapBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_static_image_map);
        binding.setViewModel(new ViewModel());
        showBackButton(true);
    }

    public class ViewModel {

        public void onGetImageMapButtonClick(View view) {
            getStaticImageMap();
        }

        private void getStaticImageMap() {

            if (binding.etCenter.getText().toString().isEmpty()) {
                showAlertDialog(null, "Center can\'t be blank!");
                return;
            }

            Map<String, String> params = new ArrayMap<>();
            params.put(CENTER, binding.etCenter.getText().toString());
            params.put(ZOOM, String.valueOf(binding.sbZoom.getProgress()));
            params.put(SIZE, "300x550");
            params.put(MAP_TYPE, "roadmap");
            params.put(SCALE, String.valueOf(binding.sbScale.getProgress()));
            params.put(KEY, getString(R.string.google_app_api_key));
            Glide.with(StaticImageMapActivity.this)
                    .load(UrlBuilder.buildUrl(params))
                    .crossFade()
                    .into(binding.ivStaticMap);
        }
    }

}
