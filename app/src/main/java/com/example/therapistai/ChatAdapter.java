package com.example.therapistai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * RecyclerView Adapter for displaying chat messages in Therapist AI
 */
public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    
    private List<ChatMessage> chatMessages;
    
    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }
    
    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        holder.bind(message);
    }
    
    @Override
    public int getItemCount() {
        return chatMessages.size();
    }
    
    /**
     * ViewHolder class for chat messages
     */
    static class ChatViewHolder extends RecyclerView.ViewHolder {
        
        private CardView cvUserMessage;
        private CardView cvAiMessage;
        private TextView tvUserMessage;
        private TextView tvAiMessage;
        
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            
            cvUserMessage = itemView.findViewById(R.id.cvUserMessage);
            cvAiMessage = itemView.findViewById(R.id.cvAiMessage);
            tvUserMessage = itemView.findViewById(R.id.tvUserMessage);
            tvAiMessage = itemView.findViewById(R.id.tvAiMessage);
        }
        
        /**
         * Bind chat message data to the view
         * @param message The ChatMessage to display
         */
        public void bind(ChatMessage message) {
            if (message.isUserMessage()) {
                // Show user message, hide AI message
                cvUserMessage.setVisibility(View.VISIBLE);
                cvAiMessage.setVisibility(View.GONE);
                tvUserMessage.setText(message.getMessage());
            } else {
                // Show AI message, hide user message
                cvUserMessage.setVisibility(View.GONE);
                cvAiMessage.setVisibility(View.VISIBLE);
                tvAiMessage.setText(message.getMessage());
            }
        }
    }
}