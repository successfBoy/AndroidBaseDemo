package com.lpc.androidbasedemo.scribble.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.lpc.androidbasedemo.R;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * created by ruanyandong
 * time：2018/8/17
 * 13439408311
 *
 * @author tal
 */

public class ScribbleBarLayout extends FrameLayout {

    /**
     * 涂鸦工具条
     */
    private ConstraintLayout scribbleToolBar;
    private ImageView imgHand;
    private ImageView imgPenSizeMax;
    private ImageView imgPenSizeMiddle;
    private ImageView imgPenSizeMin;
    private LinearLayout middleLayout;
    private ImageView imgMiddleLayoutLeft;
    private ImageView imgMiddleLayoutMiddle;
    private ImageView imgMiddleLayoutRight;
    private ImageView imgBackout;
    private ImageView imgUnBackout;

    /**
     * 颜色选择条
     * red blue green orange_dark orange_light pink purple black
     */
    private LinearLayout scribbleColorBar;
    private ImageView imgRed;
    private ImageView imgBlue;
    private ImageView imgGreen;
    private ImageView imgOrangeDark;
    private ImageView imgOrangeLight;
    private ImageView imgPink;
    private ImageView imgPurple;
    private ImageView imgBlack;

    /**
     * 颜色选择条的展开和收缩动画
     *
     * @param context
     */
    private Animation expandAnimation;
    private Animation shrinkAnimation;
    private Animation shrinkAnimationIn;

    /**
     * 选择了除黑色以外的哪个颜色
     */
    private Colors whichClick = Colors.BLUE;
    private boolean isClickBlack = false;
    private List<ImageView> penColors;

    /**
     * 传值接口
     */
    private ScribbleClickListener listener;

    public void setListener(ScribbleClickListener listener) {
        this.listener = listener;

        // 默认是随机值
        final int index = new Random().nextInt(Colors.values().length);
        this.listener.onColorClick(Colors.values()[index].color);
        post(new Runnable() {
            @Override
            public void run() {
                if (penColors.get(index) != null) {
                    penColors.get(index).callOnClick();
                }
            }
        });

        // 默认笔画粗细
        this.listener.onBushClick(PenSize.MIDDLE);
    }

    public ScribbleBarLayout(Context context) {
        this(context, null);
    }

    public ScribbleBarLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScribbleBarLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View viewColorBar = LayoutInflater.from(context).inflate(R.layout.doodle_scribble_color_bar_layout, null);
        addView(viewColorBar);

        scribbleColorBar = findViewById(R.id.scribble_color_bar);
        imgRed = findViewById(R.id.color_red);
        imgBlue = findViewById(R.id.color_blue);
        imgGreen = findViewById(R.id.color_green);
        imgOrangeDark = findViewById(R.id.color_orange_dark);
        imgOrangeLight = findViewById(R.id.color_orange_light);
        imgPink = findViewById(R.id.color_pink);
        imgPurple = findViewById(R.id.color_purple);
        imgBlack = findViewById(R.id.color_black);

        penColors = Arrays.asList(imgRed, imgBlue, imgGreen, imgOrangeDark, imgOrangeLight, imgPink, imgPurple, imgBlack);

        View viewToolBar = LayoutInflater.from(context).inflate(R.layout.doodle_scribble_tool_bar_layout, null);
        addView(viewToolBar);

        scribbleToolBar = viewToolBar.findViewById(R.id.scribble_tool_bar);
        imgHand = viewToolBar.findViewById(R.id.hand);
        imgPenSizeMax = viewToolBar.findViewById(R.id.pen_large_size);
        imgPenSizeMiddle = viewToolBar.findViewById(R.id.pen_middle_size);
        imgPenSizeMin = viewToolBar.findViewById(R.id.pen_small_size);
        middleLayout = viewToolBar.findViewById(R.id.middle_layout);
        imgMiddleLayoutLeft = viewToolBar.findViewById(R.id.colors_btn);
        imgMiddleLayoutMiddle = viewToolBar.findViewById(R.id.middle_color);
        imgMiddleLayoutRight = viewToolBar.findViewById(R.id.right_color);
        imgBackout = viewToolBar.findViewById(R.id.back_out);
        imgUnBackout = viewToolBar.findViewById(R.id.unrevoke);

        // 默认选中中间笔画
        imgPenSizeMiddle.setImageResource(R.mipmap.doodle_middle_press);

        imgHand.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHand.setSelected(true);
                imgPenSizeMax.setImageResource(R.mipmap.doodle_large_width);
                imgPenSizeMiddle.setImageResource(R.mipmap.doodle_middle);
                imgPenSizeMin.setImageResource(R.mipmap.doodle_small_width);
                listener.onFingerClick(imgHand.isSelected());
            }
        });

        imgPenSizeMax.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHand.setSelected(false);
                imgPenSizeMax.setImageResource(R.mipmap.doodle_large_width_press);
                imgPenSizeMiddle.setImageResource(R.mipmap.doodle_middle);
                imgPenSizeMin.setImageResource(R.mipmap.doodle_small_width);

                listener.onBushClick(PenSize.MAX);
                listener.setAuthorize();
            }
        });

        imgPenSizeMiddle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHand.setSelected(false);
                imgPenSizeMax.setImageResource(R.mipmap.doodle_large_width);
                imgPenSizeMiddle.setImageResource(R.mipmap.doodle_middle_press);
                imgPenSizeMin.setImageResource(R.mipmap.doodle_small_width);

                listener.onBushClick(PenSize.MIDDLE);
                listener.setAuthorize();
            }
        });

        imgPenSizeMin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgHand.setSelected(false);
                imgPenSizeMax.setImageResource(R.mipmap.doodle_large_width);
                imgPenSizeMiddle.setImageResource(R.mipmap.doodle_middle);
                imgPenSizeMin.setImageResource(R.mipmap.doodle_small_width_press);

                listener.onBushClick(PenSize.MIN);
                listener.setAuthorize();
            }
        });

        /**
         * 动画
         */
        expandAnimation = AnimationUtils.loadAnimation(context, R.anim.doodle_scribble_color_bar_expand_animation);
        shrinkAnimation = AnimationUtils.loadAnimation(context, R.anim.doodle_scribble_tool_bar_shrink_animation_in);

        expandAnimation.setAnimationListener(createAnimationListener());
        shrinkAnimation.setAnimationListener(createAnimationListener());

        expandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.refresh();
                scribbleColorBar.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        shrinkAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.refresh();
                scribbleColorBar.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imgMiddleLayoutLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canSelect()) {
                    // 选择组件时不可点击
                    return;
                }
                scribbleToolBar.setVisibility(View.GONE);
                scribbleColorBar.setVisibility(View.VISIBLE);
                scribbleColorBar.startAnimation(expandAnimation);
                isClickBlack = false;
            }
        });

        imgRed.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_red_style);
                imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_red_style);
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

                scribbleColorBar.setVisibility(View.GONE);
                scribbleToolBar.setVisibility(View.VISIBLE);
                scribbleToolBar.startAnimation(shrinkAnimation);

                listener.onColorClick("#ffff0000");

                whichClick = Colors.RED;

                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

            }
        });

        imgBlue.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_blue_style);
                imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_blue_style);
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

                scribbleColorBar.setVisibility(View.GONE);
                scribbleToolBar.setVisibility(View.VISIBLE);
                scribbleToolBar.startAnimation(shrinkAnimation);

                listener.onColorClick("#ff33b5e5");
                whichClick = Colors.BLUE;

                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

            }
        });

        imgGreen.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_green_style);
                imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_green_style);
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

                scribbleColorBar.setVisibility(View.GONE);
                scribbleToolBar.setVisibility(View.VISIBLE);
                scribbleToolBar.startAnimation(shrinkAnimation);

                listener.onColorClick("#ff669900");
                whichClick = Colors.GREEN;

                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

            }
        });

        imgOrangeDark.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_orange_dark_style);
                imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_orange_dark_style);
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

                scribbleColorBar.setVisibility(View.GONE);
                scribbleToolBar.setVisibility(View.VISIBLE);
                scribbleToolBar.startAnimation(shrinkAnimation);

                listener.onColorClick("#ffff8800");
                whichClick = Colors.ORANGE_DARK;

                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

            }
        });

        imgOrangeLight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_orange_light_style);
                imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_orange_light_style);
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

                scribbleColorBar.setVisibility(View.GONE);
                scribbleToolBar.setVisibility(View.VISIBLE);
                scribbleToolBar.startAnimation(shrinkAnimation);

                listener.onColorClick("#ffffbb33");
                whichClick = Colors.ORANGE_LIGHT;

                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);
            }
        });

        imgPink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_pink_style);
                imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_pink_style);
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

                scribbleColorBar.setVisibility(View.GONE);
                scribbleToolBar.setVisibility(View.VISIBLE);
                scribbleToolBar.startAnimation(shrinkAnimation);

                listener.onColorClick("#FF4081");
                whichClick = Colors.PINK;

                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);
            }
        });

        imgPurple.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_purple_style);
                imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_purple_style);
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);

                scribbleColorBar.setVisibility(View.GONE);
                scribbleToolBar.setVisibility(View.VISIBLE);
                scribbleToolBar.startAnimation(shrinkAnimation);

                listener.onColorClick("#ffaa66cc");
                whichClick = Colors.PURPLE;

                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);
            }
        });

        imgBlack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_black_style);
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_black_style);

                scribbleColorBar.setVisibility(View.GONE);
                scribbleToolBar.setVisibility(View.VISIBLE);
                scribbleToolBar.startAnimation(shrinkAnimation);

                listener.onColorClick("#ff000000");
                isClickBlack = true;

                if (Colors.RED == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
                } else if (Colors.BLUE == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                } else if (Colors.GREEN == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                } else if (Colors.ORANGE_DARK == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                } else if (Colors.ORANGE_LIGHT == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                } else if (Colors.PINK == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                } else if (Colors.PURPLE == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                }

                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
            }
        });

        imgMiddleLayoutMiddle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Colors.RED == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_red_style);
                    imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_red_style);
                    listener.onColorClick("#ffcc0000");
                } else if (Colors.BLUE == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_blue_style);
                    imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_blue_style);
                    listener.onColorClick("#ff33b5e5");
                } else if (Colors.GREEN == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_green_style);
                    imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_green_style);
                    listener.onColorClick("#ff669900");
                } else if (Colors.ORANGE_DARK == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_orange_dark_style);
                    imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_orange_dark_style);
                    listener.onColorClick("#ffff8800");
                } else if (Colors.ORANGE_LIGHT == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_orange_light_style);
                    imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_orange_light_style);
                    listener.onColorClick("#ffffbb33");
                } else if (Colors.PINK == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_pink_style);
                    imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_pink_style);
                    listener.onColorClick("#FF4081");
                } else if (Colors.PURPLE == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_purple_style);
                    imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_purple_style);
                    listener.onColorClick("#ffaa66cc");
                }

                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);
                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_black_style);
            }
        });

        imgMiddleLayoutRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                imgMiddleLayoutRight.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_black_style);

                listener.onColorClick("#ff000000");
                isClickBlack = true;
                if (Colors.RED == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
                } else if (Colors.BLUE == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                } else if (Colors.GREEN == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                } else if (Colors.ORANGE_DARK == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                } else if (Colors.ORANGE_LIGHT == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                } else if (Colors.PINK == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                } else if (Colors.PURPLE == whichClick) {
                    imgMiddleLayoutMiddle.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                }

                imgBlack.setImageResource(R.drawable.doodle_scribble_bar_circle_stroke_black_style);
                imgBlue.setImageResource(R.drawable.doodle_scribble_bar_circle_blue_style);
                imgGreen.setImageResource(R.drawable.doodle_scribble_bar_circle_green_style);
                imgOrangeDark.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_dark_style);
                imgOrangeLight.setImageResource(R.drawable.doodle_scribble_bar_circle_orange_light_style);
                imgPink.setImageResource(R.drawable.doodle_scribble_bar_circle_pink_style);
                imgPurple.setImageResource(R.drawable.doodle_scribble_bar_circle_purple_style);
                imgRed.setImageResource(R.drawable.doodle_scribble_bar_circle_red_style);
            }
        });

        /**
         * 撤销操作
         */
        imgBackout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.back();
            }
        });

        /**
         * 恢复操作
         */
        imgUnBackout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.forward();
            }
        });
    }

    /**
     * 每次动画结束时，要清理控件持有的动画，否则会阻塞控件的一些点击事件和设置其他属性的操作
     *
     * @return
     */
    private Animation.AnimationListener createAnimationListener() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                scribbleColorBar.clearAnimation();
                scribbleToolBar.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }

    public void setBackEnable(boolean isEnable) {

        imgBackout.setEnabled(isEnable);
    }

    public void setForwardEnable(boolean isEnable) {

        imgUnBackout.setEnabled(isEnable);
    }

    /**
     * 返回用户是否可以点击课件上的组件
     *
     * @return
     */
    public boolean canSelect() {
        return imgHand.isSelected();
    }

    /**
     * 传递画笔尺寸和颜色的接口
     */
    public interface ScribbleClickListener {
        /**
         * 传递画笔尺寸
         *
         * @param penSize
         */
        void onBushClick(PenSize penSize);

        /**
         * 传递选择颜色
         *
         * @param color
         */
        void onColorClick(String color);

        /**
         * 反撤销
         */
        void forward();

        /**
         * 撤销
         */
        void back();

        void onFingerClick(boolean canSelect);

        void setAuthorize();

        void refresh();


    }

    /**
     * 画笔尺寸枚举
     */
    public enum PenSize {
        MAX, MIDDLE, MIN
    }

    public enum Colors {
        RED("#ffcc0000"), BLUE("#ff33b5e5"), GREEN("#ff669900"), ORANGE_DARK("#ffff8800"), ORANGE_LIGHT("#ffffbb33"), PINK("#FF4081"), PURPLE("#ffaa66cc"), BLACK("#ff000000");
        String color;

        Colors(String color) {
            this.color = color;
        }
    }

}
