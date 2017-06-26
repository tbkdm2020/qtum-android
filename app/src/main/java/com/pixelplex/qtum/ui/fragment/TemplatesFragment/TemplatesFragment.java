package com.pixelplex.qtum.ui.fragment.TemplatesFragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.pixelplex.qtum.R;
import com.pixelplex.qtum.datastorage.TinyDB;
import com.pixelplex.qtum.model.ContractTemplate;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragment;
import com.pixelplex.qtum.ui.fragment.BaseFragment.BaseFragmentPresenterImpl;
import com.pixelplex.qtum.utils.DateCalculator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class TemplatesFragment extends BaseFragment implements TemplatesFragmentView, TemplateSelectListener {

    public final int LAYOUT = R.layout.fragment_templates;

    public static TemplatesFragment newInstance() {
        Bundle args = new Bundle();
        TemplatesFragment fragment = new TemplatesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    TemplatesFragmentPresenterImpl presenter;

    @BindView(R.id.recycler_view)
    RecyclerView contractList;

    @OnClick(R.id.ibt_back)
    public void onClick() {
        getActivity().onBackPressed();
    }

    @Override
    protected void createPresenter() {
        presenter = new TemplatesFragmentPresenterImpl(this);
    }

    @Override
    protected BaseFragmentPresenterImpl getPresenter() {
        return presenter;
    }

    @Override
    protected int getLayout() {
        return LAYOUT;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        contractList.setLayoutManager(new LinearLayoutManager(getContext()));
        TinyDB tinyDB = new TinyDB(getContext());
        List<ContractTemplate> contractTemplateList = tinyDB.getContractTemplateList();

        List<ContractTemplate> contractFullTemplateList = new ArrayList<>();
        for(ContractTemplate contractTemplate : contractTemplateList){
            if(contractTemplate.isFullContractTemplate()){
                contractFullTemplateList.add(contractTemplate);
            }
        }

        Collections.sort(contractFullTemplateList, new Comparator<ContractTemplate>() {
            @Override
            public int compare(ContractTemplate contractInfo, ContractTemplate t1) {
                return DateCalculator.equals(contractInfo.getDate(),t1.getDate());
            }
        });

        contractList.setAdapter(new TemplatesRecyclerAdapter(contractFullTemplateList,this));
    }

    @Override
    public void onSelectContract(long uiid) {
        presenter.openConstructorByName(uiid);
    }
}
