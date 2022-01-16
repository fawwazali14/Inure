package app.simple.inure.decorations.theme;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import app.simple.inure.R;
import app.simple.inure.themes.interfaces.ThemeChangedListener;
import app.simple.inure.themes.manager.Theme;
import app.simple.inure.themes.manager.ThemeManager;
import app.simple.inure.util.ColorUtils;
import top.defaults.drawabletoolbox.DrawableBuilder;

// TODO - make a custom seekbar
public class ThemeSeekBar extends AppCompatSeekBar implements ThemeChangedListener {
    
    private Drawable thumb;
    
    public ThemeSeekBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    public ThemeSeekBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init() {
        setThumb();
        setProgress();
    }
    
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ThemeManager.INSTANCE.addListener(this);
    }
    
    @Override
    public void onThemeChanged(Theme theme) {
        setThumb();
    }
    
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ThemeManager.INSTANCE.removeListener(this);
    }
    
    private void setThumb() {
        setThumb(new DrawableBuilder()
                .oval()
                .width(getResources().getDimensionPixelOffset(R.dimen.seekbar_thumb_size))
                .height(getResources().getDimensionPixelOffset(R.dimen.seekbar_thumb_size))
                .ripple(false)
                .strokeColor(ColorUtils.INSTANCE.resolveAttrColor(getContext(), R.attr.colorAppAccent))
                .strokeWidth(getResources().getDimensionPixelOffset(R.dimen.seekbar_stroke_size))
                .solidColor(ThemeManager.INSTANCE.getTheme().getViewGroupTheme().getBackground())
                .build());
        
        invalidate();
    }
    
    private void setProgress() {
        Drawable[] drawables = {
                new DrawableBuilder()
                        .rounded()
                        .solidColor(Color.YELLOW)
                        .ripple(false)
                        .build(),
                
                new DrawableBuilder()
                        .rounded()
                        .solidColor(Color.GREEN)
                        .ripple(false)
                        .build()};
        
        LayerDrawable layerDrawable = new LayerDrawable(drawables);
        layerDrawable.setId(0, android.R.id.background);
        layerDrawable.setId(1, android.R.id.progress);
        
        layerDrawable.getDrawable(1).setTintList(ColorStateList.valueOf(ColorUtils.INSTANCE.resolveAttrColor(getContext(), R.attr.colorAppAccent)));
        
        setProgressBackgroundTintList(ColorStateList.valueOf(
                ColorUtils.INSTANCE.lightenColor(
                        ColorUtils.INSTANCE.resolveAttrColor(getContext(), R.attr.colorAppAccentLight),
                        0.8f)));
    }
    
    private void setProgressDrawable() {
        float r = 20;
        ShapeDrawable shape = new ShapeDrawable(new RoundRectShape(new float[] {r, r, r, r, r, r, r, r}, null, null));
        setProgressDrawable(shape);
    }
}