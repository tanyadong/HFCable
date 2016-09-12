package com.hbhongfei.hfcable.util;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hbhongfei.hfcable.R;

/**
 * �����Ҫ����ˢ��ֱ����canPullDown�з���false��������Զ����غ�����ˢ��û�г�ͻ��ͨ��������β����footerviewʵ���Զ����أ�
 * ������ʹ���в�Ҫ�ٶ�footerview��
 * ��������http://blog.csdn.net/zhongkejingwang/article/details/38963177
 * @author chenjing
 * 
 */
public class PullableListView extends ListView implements Pullable
{
	public static final int INIT = 0;
	public static final int LOADING = 1;
	private OnLoadListener mOnLoadListener;
	private ImageView mLoadingView;
	private TextView mStateTextView;
	private int state = INIT;
	private boolean canLoad = true;
	private AnimationDrawable mLoadAnim;

	public PullableListView(Context context)
	{
		super(context);
		init(context);
	}

	public PullableListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}

	public PullableListView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context)
	{
		View view = LayoutInflater.from(context).inflate(R.layout.load_more,
				null);
		mLoadingView = (ImageView) view.findViewById(R.id.loading_icon);

		mLoadAnim = (AnimationDrawable) mLoadingView.getBackground();
		mStateTextView = (TextView) view.findViewById(R.id.loadstate_tv);
		addFooterView(view, null, false);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		switch (ev.getActionMasked())
		{
		case MotionEvent.ACTION_DOWN:
			// ���µ�ʱ���ֹ�Զ�����
			canLoad = false;
			break;
		case MotionEvent.ACTION_UP:
			// �ɿ����ж��Ƿ��Զ�����
			canLoad = true;
			checkLoad();
			break;
		}
		return super.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt)
	{
		super.onScrollChanged(l, t, oldl, oldt);
		// �ڹ������ж��Ƿ������Զ���������
		checkLoad();
	}

	/**
	 * �ж��Ƿ������Զ���������
	 */
	private void checkLoad()
	{
		if (reachBottom() && mOnLoadListener != null && state != LOADING
				&& canLoad)
		{
			mOnLoadListener.onLoad(this);
			changeState(LOADING);
		}
	}

	private void changeState(int state)
	{
		this.state = state;
		switch (state)
		{
		case INIT:
//			mLoadAnim.stop();
			mLoadingView.setVisibility(View.INVISIBLE);
//			mStateTextView.setText(R.string.more);
			break;

		case LOADING:
			mLoadingView.setVisibility(View.VISIBLE);
//			mLoadAnim.start();
			mStateTextView.setText("正在加载中");
			break;
		}
	}

	/**
	 * ��ɼ���
	 */
	public void finishLoading()
	{
		changeState(INIT);
	}

	@Override
	public boolean canPullDown()
	{
		if (getCount() == 0)
		{
			// û��item��ʱ��Ҳ��������ˢ��
			return true;
		} else if (getFirstVisiblePosition() == 0
				&& getChildAt(0).getTop() >= 0)
		{
			// ����ListView�Ķ�����
			return true;
		} else
			return false;
	}

	public void setOnLoadListener(OnLoadListener listener)
	{
		this.mOnLoadListener = listener;
	}

	/**
	 * @return footerview�ɼ�ʱ����true�����򷵻�false
	 */
	public boolean reachBottom()
	{
		if (getCount() == 0)
		{
			// û��item��ʱ��Ҳ������������
			return true;
		} else if (getLastVisiblePosition() == (getCount() - 1))
		{
			// �����ײ���
			if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition()) != null
					&& getChildAt(
							getLastVisiblePosition()
									- getFirstVisiblePosition()).getTop() < getMeasuredHeight())
				return true;
		}
		return false;
	}

	public interface OnLoadListener
	{
		void onLoad(PullableListView pullableListView);
	}
}
