package com.example.orthotrainer;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orthotrainer.model.Rule;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class RuleAdapter extends RecyclerView.Adapter<RuleAdapter.RuleViewHolder> {

    private static final int FREE_RULE_COUNT = 4;

    private final List<Rule> rules;
    private final Context context;


    public RuleAdapter(Context context, List<Rule> rules) {

        this.context = context;
        this.rules = rules;

    }

    @NonNull
    @Override
    public RuleViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rule, parent, false);

        return new RuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull RuleViewHolder holder,
            int position
    ) {

        Rule rule = rules.get(position);

        holder.title.setText(rule.getTitle());
        holder.category.setText(rule.getCategory());



        boolean completed =
                isRuleCompleted(rule.getId());

        /*
         * Если тема уже пройдена —
         * показываем зелёную карточку.
         */
        if (completed ) {

            holder.ruleCard.setCardBackgroundColor(
                    context.getColor(R.color.correct_answer)
            );

        } else {

            holder.ruleCard.setCardBackgroundColor(
                    context.getColor(R.color.surface)
            );
        }

        /*
         * Показываем замок для Premium-тем.
         */


        holder.itemView.setOnClickListener(v -> {



            /*
             * Открытая тема.
             */
            Intent intent = new Intent(
                    context,
                    RuleDetailActivity.class
            );

            intent.putExtra(
                    "RULE_ID",
                    rule.getId()
            );

            context.startActivity(intent);
        });
    }

    private boolean isRuleCompleted(int ruleId) {

        return context
                .getSharedPreferences(
                        "progress",
                        Context.MODE_PRIVATE
                )
                .getBoolean(
                        "rule_" + ruleId + "_completed",
                        false
                );
    }

    @Override
    public int getItemCount() {

        return rules.size();
    }

    static class RuleViewHolder
            extends RecyclerView.ViewHolder {

        TextView title;
        TextView category;
        MaterialCardView ruleCard;

        RuleViewHolder(View itemView) {

            super(itemView);

            title = itemView.findViewById(
                    R.id.ruleTitle
            );

            category = itemView.findViewById(
                    R.id.ruleCategory
            );

            ruleCard = itemView.findViewById(
                    R.id.ruleCard
            );
        }
    }
}