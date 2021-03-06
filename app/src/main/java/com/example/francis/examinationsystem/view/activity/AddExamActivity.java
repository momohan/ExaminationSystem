package com.example.francis.examinationsystem.view.activity;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.francis.examinationsystem.R;
import com.example.francis.examinationsystem.base.MVPBaseActivity;
import com.example.francis.examinationsystem.contract.IAddExamView;
import com.example.francis.examinationsystem.entity.ExamPaper;
import com.example.francis.examinationsystem.entity.Subject;
import com.example.francis.examinationsystem.global.Constants;
import com.example.francis.examinationsystem.presenter.AddExamPresenter;
import com.example.francis.examinationsystem.util.Toaster;
import com.example.francis.examinationsystem.view.adapter.ChooseSubjectAdapte;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Francis on 2017/3/21.
 */

public class AddExamActivity extends MVPBaseActivity<IAddExamView, AddExamPresenter> implements IAddExamView,ChooseSubjectAdapte.CallBack
,CompoundButton.OnCheckedChangeListener{
    @BindView(R.id.toolbar_main)
    Toolbar toolbarMain;
    @BindView(R.id.btn_exam_judge)
    FloatingActionButton btnExamJudge;
    @BindView(R.id.btn_exam_single_selection)
    FloatingActionButton btnExamSingleSelection;
    @BindView(R.id.btn_exam_multiple_selection)
    FloatingActionButton btnExamMultipleSelection;
    @BindView(R.id.btn_exam_short_answer)
    FloatingActionButton btnExamShortAnswer;
    @BindView(R.id.btn_exam_add)
    FloatingActionsMenu btnExamAdd;
    @BindView(R.id.list_exam)
    RecyclerView listExam;

    private BaseQuickAdapter mChooseSubjectAdapte;
    private List<Subject> titles;

    private ExamPaper examPaper;

    private BaseQuickAdapter mSearchSubjectAdapter;
    private List<Subject> searchSubjects;
    private List<Subject> checkSubjects;
    private CheckBox cbChooseExamAll;
    private int position=-1;




    @Override
    public void showToast(String message) {
        Toaster.showShort(message);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_addexam;
    }

    @Override
    protected void initData() {
        if (getIntent()!=null){
            examPaper= (ExamPaper) getIntent().getSerializableExtra("examPaper");

        }

        titles = new ArrayList<>();
        mChooseSubjectAdapte = new ChooseSubjectAdapte(R.layout.item_choose_exam_subject, titles,this,1);
        listExam.setAdapter(mChooseSubjectAdapte);

        searchSubjects=new ArrayList<>();
        checkSubjects=new ArrayList<>();

        mChooseSubjectAdapte.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AddExamActivity.this.position=position;
                Intent intent=new Intent();
                intent.putExtra("subject",titles.get(position));
                intent.putExtra("examType",Integer.valueOf(String.valueOf(titles.get(position).getSubjectTypeId())));
                toForResult(TitleDetailsActivity.class,intent,1);
            }
        });
    }

    @Override
    protected void initView() {
        setToolBar(toolbarMain, "试卷标题");
        listExam.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected AddExamPresenter createPresenter() {
        return new AddExamPresenter();
    }


    @OnClick({R.id.btn_exam_judge, R.id.btn_exam_single_selection, R.id.btn_exam_multiple_selection, R.id.btn_exam_short_answer, R.id.btn_exam_add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_exam_judge:
                showSubjectChooseDialog(1);
                break;
            case R.id.btn_exam_single_selection:
                showSubjectChooseDialog(3);
                break;
            case R.id.btn_exam_multiple_selection:
                showSubjectChooseDialog(2);
                break;
            case R.id.btn_exam_short_answer:
                showSubjectChooseDialog(4);
                break;
            case R.id.btn_exam_add:
                break;
        }
    }

    private void showSubjectChooseDialog(final int type) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_choose_exam_subject, null);
        RecyclerView listChooseExamSubjects = (RecyclerView) view.findViewById(R.id.list_choose_exam_subject);
        cbChooseExamAll = (CheckBox) view.findViewById(R.id.cb_choose_exam_allCheck);
        Button btnChooseExamCreate = (Button) view.findViewById(R.id.btn_choose_exam_create);
        Button btnChooseExamAdd = (Button) view.findViewById(R.id.btn_choose_exam_add);
        final EditText etChooseExamSearch = (EditText) view.findViewById(R.id.et_choose_exam_search);
        mPresenter.searchSubject(etChooseExamSearch.getText().toString(),type);

        etChooseExamSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.searchSubject(s.toString(),type);
            }
        });

        listChooseExamSubjects.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSearchSubjectAdapter=new ChooseSubjectAdapte(R.layout.item_choose_exam_subject, searchSubjects,this,2);

        listChooseExamSubjects.setAdapter(mSearchSubjectAdapter);

        cbChooseExamAll.setOnCheckedChangeListener(this);


        btnChooseExamAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titles.addAll(checkSubjects);
                mChooseSubjectAdapte.notifyDataSetChanged();
                dialog.dismiss();
            }
        });


        btnChooseExamCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (type) {
                    case 3:
                        intent.putExtra("examType", Constants.ExamType.EXAM_JUDGE);
                        toForResult(TitleDetailsActivity.class, intent, 0);
                        break;
                    case 1:
                        intent.putExtra("examType", Constants.ExamType.EXAM_SINGLE);
                        toForResult(TitleDetailsActivity.class, intent, 0);
                        break;
                    case 2:
                        intent.putExtra("examType", Constants.ExamType.EXAM_MUTIPLE);
                        toForResult(TitleDetailsActivity.class, intent, 0);
                        break;
                    case 4:
                        intent.putExtra("examType", Constants.ExamType.EXAM_SHORT);
                        toForResult(TitleDetailsActivity.class, intent, 0);
                        break;
                }
                dialog.dismiss();
            }
        });

        dialog.setView(view);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_exam_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.send) {
//            mPresenter.addSubjects(titles);
            mPresenter.addExamPager(examPaper,titles);
        }
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            if (data != null) {
                Subject subject = (Subject) data.getSerializableExtra("subject");
                titles.add(subject);
                mChooseSubjectAdapte.notifyDataSetChanged();
            }
        }
        if (requestCode==1&&resultCode==1){
            if (data != null) {
                Subject subject = (Subject) data.getSerializableExtra("subject");
                titles.set(position,subject);
                mChooseSubjectAdapte.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void addSubjectsSuccess(List<Long> subjectIds) {
//        mPresenter.addExamPager(subjectIds,examPaper);
    }

    @Override
    public void addExamPaperSuccess(ExamPaper examPaper) {
//        Intent intent=new Intent();
//        intent.putExtra("examPaper",examPaper);
//        to(ExaminationActivity.class,new Intent());
        finish();
    }

    @Override
    public void searchSubjectSuccess(List<Subject> searchSubjects) {
        if (mSearchSubjectAdapter!=null){
            this.searchSubjects.clear();
            this.searchSubjects.addAll(searchSubjects);
            mSearchSubjectAdapter.notifyDataSetChanged();

            checkSubjects.clear();
            cbChooseExamAll.setOnCheckedChangeListener(null);
            cbChooseExamAll.setChecked(false);
            cbChooseExamAll.setOnCheckedChangeListener(this);
        }

    }

    @Override
    public void checkSubjectList(List<Subject> checkSujects) {
        this.checkSubjects.clear();
        this.checkSubjects=checkSujects;
        if (cbChooseExamAll!=null ){
            if (checkSujects.size()==searchSubjects.size()){
                cbChooseExamAll.setOnCheckedChangeListener(null);
                cbChooseExamAll.setChecked(true);
                cbChooseExamAll.setOnCheckedChangeListener(this);

            }else{
                cbChooseExamAll.setOnCheckedChangeListener(null);
                cbChooseExamAll.setChecked(false);
                cbChooseExamAll.setOnCheckedChangeListener(this);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        for (int i=0;i<searchSubjects.size();i++) {
            searchSubjects.get(i).setChecked(isChecked);
        }
        mSearchSubjectAdapter.notifyDataSetChanged();
        checkSubjects.clear();
        if (isChecked){
            checkSubjects.addAll(searchSubjects);
        }
    }
}
