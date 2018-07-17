package com.example.yuanyuanlai.drivenoworry.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanyuanlai.drivenoworry.Base.BaseFragment;
import com.example.yuanyuanlai.drivenoworry.R;

import org.w3c.dom.Text;

public class MyFragment extends BaseFragment implements View.OnClickListener,CompoundButton.OnCheckedChangeListener{

    private RelativeLayout setPhoneRelativeLayout, setMessageRelativeLayout;
    private View view;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private LayoutInflater layoutInflater;
    private EditText editText;
    private TextView phoneTextView, messageTextView;
    private Switch emergencySwitch;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my,container,false);
    }

    @Override
    protected void initData() {

        sharedPreferences = getActivity().getSharedPreferences("emergency", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        boolean emergency_state = sharedPreferences.getBoolean("emergency_state", false);
        emergencySwitch.setChecked(emergency_state);
        String phone = sharedPreferences.getString("emergency_phone_number", "");
        String message = sharedPreferences.getString("emergency_message", "");
        phoneTextView.setText(phone);
        messageTextView.setText(message);

    }

    @Override
    protected void setData() {

    }

    @Override
    protected void setListener() {

        setPhoneRelativeLayout.setOnClickListener(this);
        setMessageRelativeLayout.setOnClickListener(this);
        emergencySwitch.setOnCheckedChangeListener(this);

    }

    @Override
    protected void initView(View view) {

     setPhoneRelativeLayout = (RelativeLayout)view.findViewById(R.id.set_phone_relativeLayout);
     setMessageRelativeLayout = (RelativeLayout)view.findViewById(R.id.set_message_relativeLayout);
     phoneTextView = (TextView)view.findViewById(R.id.emergency_phone_textView);
     messageTextView = (TextView)view.findViewById(R.id.emergency_message_textView);
     emergencySwitch = (Switch)view.findViewById(R.id.emergency_switch);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.set_phone_relativeLayout:
                builder = new AlertDialog.Builder(getActivity());
                layoutInflater = getActivity().getLayoutInflater();
                view = layoutInflater.inflate(R.layout.edittext_dialog, null);
                editText = (EditText)view.findViewById(R.id.edit_content);
                editText.setHint("phone number");
                builder.setView(view)
                        .setTitle("输入你的紧急号码")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    String tempText = editText.getText().toString();
                                    editor.putString("emergency_phone_number", tempText);
                                    editor.commit();
                                    if (!tempText.isEmpty()){
                                        phoneTextView.setText(tempText);
                                    }

                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.set_message_relativeLayout:
                if (!phoneTextView.getText().toString().isEmpty()){
                    builder = new AlertDialog.Builder(getActivity());
                    layoutInflater = getActivity().getLayoutInflater();
                    view = layoutInflater.inflate(R.layout.edittext_dialog, null);
                    editText = (EditText)view.findViewById(R.id.edit_content);
                    editText.setInputType(InputType.TYPE_CLASS_TEXT);
                    editText.setHint("emergency message");
                    builder.setView(view)
                            .setTitle("输入你的紧急信息")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String tempText = editText.getText().toString();
                                    editor.putString("emergency_message", tempText);
                                    editor.commit();
                                    if (!tempText.isEmpty()){
                                        messageTextView.setText(tempText);
                                    }

                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog = builder.create();
                    alertDialog.show();
                }else {
                    Toast.makeText(getActivity(), "请先填写手机号码", Toast.LENGTH_SHORT).show();
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId()){
            case R.id.emergency_switch:

                String phone = sharedPreferences.getString("emergency_phone_number", "");
                String message = sharedPreferences.getString("emergency_message", "");
                if (!phone.isEmpty()&&!message.isEmpty()){

                    if (isChecked){
                        editor.putBoolean("emergency_state", true);
                    }else {
                        editor.putBoolean("emergency_state", false);
                    }
                    editor.commit();

                }else {

                    Toast.makeText(getActivity(), "信息没有填写", Toast.LENGTH_SHORT).show();

                }

                break;
            default:
                break;
        }

    }
}
