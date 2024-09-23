package com.example.horseracing.Components;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatSeekBar;

import com.example.horseracing.R;

public class CharacterSeekBar extends AppCompatSeekBar {
    private Drawable characterDrawable;
    private Drawable tileDrawable;
    private final int tileSize = 30;
    private final int marioSize = 60;

    public CharacterSeekBar(Context context) {
        super(context);
        init(context, null);
    }

    public CharacterSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CharacterSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // Ẩn thumb và nền SeekBar
        setThumb(null);
        setProgressDrawable(null); // Ẩn luôn thanh mặc định

        if (attrs != null) {
            // Đọc các thuộc tính tùy chỉnh từ XML
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.CharacterSeekBar,
                    0, 0);

            try {
                // Lấy giá trị characterDrawable (Mario) từ XML
                characterDrawable = a.getDrawable(R.styleable.CharacterSeekBar_character);
                if (characterDrawable != null) {
                    characterDrawable.setBounds(0, 0, marioSize, marioSize);
                }

                // Lấy giá trị tileDrawable (ô đất) từ XML
                tileDrawable = a.getDrawable(R.styleable.CharacterSeekBar_tile);
            } finally {
                a.recycle(); // Giải phóng TypedArray
            }
        }

        // Tăng padding để Mario không bị cắt ở các cạnh
        setPadding(marioSize / 2, marioSize / 2, marioSize / 2, marioSize / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // Calculate width of progress bar and mario's position
        int width = getWidth();
        int height = getHeight();
        int progressX = (int) (getProgress() / (float) getMax() * width);

        // Vẽ các ô đất thành hàng ngang tương ứng với đoạn đã đi qua
        int numberOfTiles = progressX / tileSize + 1; // Số lượng ô đất cần vẽ
        for (int i = 0; i < numberOfTiles; i++) {
            int tileX = i * tileSize; // Vị trí X của mỗi ô đất
            int tileY = height / 2 - tileSize / 2; // Vị trí Y của ô đất (nằm giữa SeekBar)
            tileDrawable.setBounds(tileX, tileY, tileX + tileSize, tileY + tileSize);
            tileDrawable.draw(canvas);
        }

        // Vẽ Mario tại vị trí hiện tại của SeekBar
        int marioY = height / 2 - marioSize / 2;
        characterDrawable.setBounds(progressX, marioY, progressX + marioSize, marioY + marioSize);
        characterDrawable.draw(canvas);

        // Không gọi super.onDraw() để tránh vẽ lại SeekBar mặc định
    }
}
