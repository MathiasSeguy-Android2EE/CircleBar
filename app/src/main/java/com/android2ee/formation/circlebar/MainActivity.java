package com.android2ee.formation.circlebar;

import com.android2ee.expertise.ekito.myconso.poc.ekito.R;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	EditText edtValue,edtTarget,edtMax;
	EditText edtTargetLabel,edtTitle,edtSubtitle;
	Button btnSet;
	CircleBar circleBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		edtValue=(EditText)findViewById(R.id.value);
		edtTarget=(EditText)findViewById(R.id.target);
		edtMax=(EditText)findViewById(R.id.max);
		edtTargetLabel=(EditText)findViewById(R.id.targetLabel);
		edtTitle=(EditText)findViewById(R.id.title);
		edtSubtitle=(EditText)findViewById(R.id.subtitle);
		btnSet=(Button)findViewById(R.id.set);
		circleBar=(CircleBar)findViewById(R.id.view);
		btnSet.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				setTheValue();
			}
		});
	}
	
	private void setTheValue() {
		if(!TextUtils.isEmpty(edtValue.getText().toString())) {			
			circleBar.setValue(Integer.parseInt(edtValue.getText().toString()));
		}
		if(!TextUtils.isEmpty(edtTarget.getText().toString())) {			
			circleBar.setTarget(Integer.parseInt(edtTarget.getText().toString()));
		}
		if(!TextUtils.isEmpty(edtMax.getText().toString())) {			
			circleBar.setMax(Integer.parseInt(edtMax.getText().toString()));
		}
		if(!TextUtils.isEmpty(edtTitle.getText().toString())) {			
			circleBar.setTitle(edtTitle.getText().toString());
		}
		if(!TextUtils.isEmpty(edtSubtitle.getText().toString())) {			
			circleBar.setSubtitle(edtSubtitle.getText().toString());
		}
		if(!TextUtils.isEmpty(edtTargetLabel.getText().toString())) {			
			circleBar.setTargetLabel(edtTargetLabel.getText().toString());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
