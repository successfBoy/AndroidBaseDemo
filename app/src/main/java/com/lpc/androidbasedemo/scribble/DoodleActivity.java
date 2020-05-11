package com.lpc.androidbasedemo.scribble;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lpc.androidbasedemo.R;
import com.lpc.androidbasedemo.scribble.newservice.ScribbleManager;
import com.lpc.androidbasedemo.scribble.utils.ScribbleInteractiveListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DoodleActivity extends AppCompatActivity implements OnClickListener, ScribbleInteractiveListener {
    private static final boolean isNew = true;
    private boolean isAuthorize = false;
    private ScribbleView scribbleView;

    private AlertDialog mColorDialog;
    private AlertDialog mPaintDialog;
    private AlertDialog mShapeDialog;
    private Button mBtnBack;
    private Button mBtnForward;
    private EditText mEditTextRoomId;
    private EditText mEditTextUserId;
    private Client client;
    String host = "test-ichat.weclassroom.com"; //test-ichat.weclassroom.com test-scribbleapi.weclassroom.com 10.1.250.219
    int port = 6000; //6000 9311

    String oldHost = "test-scribbleapi.weclassroom.com";
    int oldPort = 9311;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doodle_activity_doodle);

        scribbleView = (ScribbleView) findViewById(R.id.doodle_surfaceview);

        findViewById(R.id.color_picker).setOnClickListener(this);
        findViewById(R.id.paint_picker).setOnClickListener(this);
        findViewById(R.id.eraser_picker).setOnClickListener(this);
        findViewById(R.id.shape_picker).setOnClickListener(this);
        mBtnBack = (Button)findViewById(R.id.btn_back);
        mBtnForward = (Button)findViewById(R.id.btn_forward);
        mBtnBack.setEnabled(false);
        mBtnForward.setEnabled(false);
        mEditTextRoomId = (EditText)findViewById(R.id.edit_text_roomid);
        mEditTextUserId = (EditText)findViewById(R.id.edit_text_uid);
        scribbleView.setBackForwardListener(new ScribbleView.BackOrForwardIsEnable() {
            @Override
            public void isBackEnable(boolean isBackEnable) {
                    mBtnBack.setEnabled(isBackEnable);
            }

            @Override
            public void isForwardEnable(boolean isForwardEnable) {
                    mBtnForward.setEnabled(isForwardEnable);
            }
        });
    }

    private void initScribble() {

        //dev-scribbleapi-01.weclassroom.com "test-scribbleapi.weclassroom.com"
        String roomid = mEditTextRoomId.getText().toString();
        String uid = mEditTextUserId.getText().toString();
        int uidInt = Integer.parseInt(uid);
        if (isNew){
            ScribbleManager scribbleManager = ScribbleManager.getsInstance();
            scribbleManager.getNewRoomService().setScribbleInteractiveListener(this);
            scribbleManager.getNewRoomService().connect(host,port,roomid, 2, 2, uidInt);
            //scribbleManager.openDoc(scribbleView,"E536b89229add25a96ffb306d42f82ad");
            //scribbleManager.getNewPageManager().showPage("E536b89229add25a96ffb306d42f82ad",0);
        }else {
            ScribbleManager scribbleManager = ScribbleManager.getsInstance();
            scribbleManager.setIsNew(false);
            scribbleManager.getOldRoomService().setScribbleInteractiveListener(this);
            scribbleManager.getOldRoomService().connect(oldHost,oldPort,roomid, 2, 2, uidInt);
        }

    }

   /* @Override
    public boolean onTouchEvent(MotionEvent event) {
         return scribbleView.onTouchEvent(event);
    }*/

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.color_picker) {
            showColorDialog();

        } else if (i == R.id.paint_picker) {
            showSizeDialog();

        } else if (i == R.id.eraser_picker) {
            scribbleView.setType(ScribbleView.ActionType.Path);
            scribbleView.setColor("#ffffff");

        } else if (i == R.id.shape_picker) {
            showShapeDialog();

        } else {

        }
    }

    private void showShapeDialog() {
        if (mShapeDialog == null) {
            mShapeDialog = new AlertDialog.Builder(this)
                    .setTitle("选择形状")
                    .setSingleChoiceItems(
                            new String[]{"路径", "直线", "箭头直线", "矩形", "圆形", "实心矩形",
                                    "实心圆", "三角形"}, 0,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    switch (which + 1) {
                                        case 1:
                                            scribbleView.setType(ScribbleView.ActionType.Path);
                                            break;
                                        case 2:
                                            scribbleView.setType(ScribbleView.ActionType.Line);
                                            break;
                                        case 3:
                                            scribbleView.setType(ScribbleView.ActionType.Line_Arrow);
                                            break;
                                        case 4:
                                            scribbleView.setType(ScribbleView.ActionType.Rect);
                                            break;
                                        case 5:
                                            scribbleView.setType(ScribbleView.ActionType.Circle);
                                            break;
                                        case 6:
                                            scribbleView.setType(ScribbleView.ActionType.FillecRect);
                                            break;
                                        case 7:
                                            scribbleView.setType(ScribbleView.ActionType.FilledCircle);
                                            break;
                                        case 8:
                                            scribbleView.setType(ScribbleView.ActionType.Triangle);
                                            break;
                                        default:
                                            break;
                                    }
                                    dialog.dismiss();
                                }
                            }).create();
        }
        mShapeDialog.show();
    }

    private void showSizeDialog() {
        if (mPaintDialog == null) {
            mPaintDialog = new AlertDialog.Builder(this)
                    .setTitle("选择画笔粗细")
                    .setSingleChoiceItems(new String[]{"细", "中", "粗"}, 0,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    switch (which) {
                                        case 0:
                                            scribbleView.setPaintSize(3);
                                            break;
                                        case 1:
                                            scribbleView.setPaintSize(5);
                                            break;
                                        case 2:
                                            scribbleView.setPaintSize(7);
                                            break;
                                        default:
                                            break;
                                    }

                                    dialog.dismiss();
                                }
                            }).create();
        }
        mPaintDialog.show();
    }

    private void showColorDialog() {
        if (mColorDialog == null) {
            mColorDialog = new AlertDialog.Builder(this)
                    .setTitle("选择颜色")
                    .setSingleChoiceItems(new String[]{"红色", "绿色", "蓝色"}, 0,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    switch (which) {
                                        case 0:
                                            scribbleView.setPaintColor("#ff0000");
                                            break;
                                        case 1:
                                            scribbleView.setPaintColor("#00ff00");
                                            break;
                                        case 2:
                                            scribbleView.setPaintColor("#0000ff");
                                            break;

                                        default:
                                            break;
                                    }

                                    dialog.dismiss();
                                }
                            }).create();
        }
        mColorDialog.show();
    }

    private int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0, 1, 1, "保存");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/doodle/" + System.currentTimeMillis() + ".png";
            if (!new File(path).exists()) {
                new File(path).getParentFile().mkdir();
            }
            savePicByPNG(scribbleView.getBitmap(), path);
            Toast.makeText(this, "图片保存成功，路径为" + path, Toast.LENGTH_LONG).show();
        }
        return true;
    }

	/*@Override
    public void onBackPressed() {
		if (!scribbleView.back()) {
			super.onBackPressed();
		}
	}*/

    public static void savePicByPNG(Bitmap b, String filePath) {
        FileOutputStream fos = null;
        try {
//			if (!new File(filePath).exists()) {
//				new File(filePath).createNewFile();
//			}
            fos = new FileOutputStream(filePath);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinRoom(View view) {
/*		ByteBuffer b = ByteBuffer.allocate(32);
//		b.order(ByteOrder.BIG_ENDIAN); // optional, the initial order of a byte buffer is always BIG_ENDIAN.
		b.putInt( 32);
		b.putInt( 1);
		b.putInt( 1);
		b.putInt( 20);
		b.putInt( 9);
		b.putInt( 1);
		b.putInt( 2);
		b.putInt( 9);

		byte[] result = b.array();
		client.sendData(result);*/
        initScribble();


    }

    public void connect(View view) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //101.201.30.147
//				new Server("localhost", 9311);
                client = new Client("101.201.30.147", 9311);
                return null;
            }

        }.execute();
    }

    public void back(View view) {
        if (!scribbleView.back()) {
            Log.e("back","back fail");
        }
    }

    public void forward(View view) {
        if (!scribbleView.forward()) {
            Log.e("forward","forward failed");
        }
    }


    public void changeAuthorizeState(View view) {
        isAuthorize = !isAuthorize;
        scribbleView.setAuthorize(isAuthorize);
    }
    public void exitRoom(View view){
        Log.e("exitRoom","exitRoom");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*if (isNew) {
            com.weclassroom.scribble.newservice.ScribbleManager scribbleManager = com.weclassroom.scribble.newservice.ScribbleManager.getsInstance();
            scribbleManager.release();
        }else {
            com.weclassroom.scribble.service.ScribbleManager scribbleManager = com.weclassroom.scribble.service.ScribbleManager.getsInstance();
            scribbleManager.release();
        }*/
        if (ScribbleManager.getsInstance().getNewRoomService()!=null){
            ScribbleManager.getsInstance().getNewRoomService().release();
        }
        if (ScribbleManager.getsInstance().getOldRoomService()!=null){
            ScribbleManager.getsInstance().getOldRoomService().release();
        }
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onConnectStatus(boolean status, Exception ex) {

    }

    @Override
    public void onEnterRoomStatus(boolean status) {
        //        ScribbleManager.getsInstance().getPageManager().showPage(0);
        if (ScribbleManager.getsInstance().isNew()&&status){//E536b89229add25a96ffb306d42f82ad
            ScribbleManager.getsInstance().openDoc(scribbleView,"E536b89229add25a96ffb306d42f82ad");
            ScribbleManager.getsInstance().getNewPageManager().showPage("E536b89229add25a96ffb306d42f82ad",0);
        }else if (!ScribbleManager.getsInstance().isNew()&&status){
            ScribbleManager.getsInstance().openDoc(scribbleView,"E536b89229add25a96ffb306d42f82ad");
            ScribbleManager.getsInstance().getOldPageManager().showPage("E536b89229add25a96ffb306d42f82ad",0);
        }

    }

    @Override
    public void onExitRoomStatus(boolean stauts) {

    }
}