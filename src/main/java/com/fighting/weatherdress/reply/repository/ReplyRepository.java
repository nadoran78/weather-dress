package com.fighting.weatherdress.reply.repository;

import com.fighting.weatherdress.post.entity.Post;
import com.fighting.weatherdress.reply.entity.Reply;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
  List<Reply> findAllByPost(Post post, Pageable pageable);
}
