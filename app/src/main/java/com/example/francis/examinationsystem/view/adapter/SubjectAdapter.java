package com.example.francis.examinationsystem.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.francis.examinationsystem.entity.Subject;

import java.util.List;

/**
 * Created by Francis on 2017/3/22.
 */

public class SubjectAdapter extends BaseQuickAdapter<Subject,BaseViewHolder>{
    public SubjectAdapter(int layoutResId, List<Subject> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Subject item) {

    }
}
