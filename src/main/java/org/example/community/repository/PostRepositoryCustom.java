package org.example.community.repository;

import lombok.RequiredArgsConstructor;
import org.example.community.dto.PostListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryCustom {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PostListDto> postListRowMapper = (rs, rowNum) -> new PostListDto(
            rs.getLong("id"),
            rs.getString("title"),
            rs.getString("nickname"),
            rs.getInt("view_count"),
            rs.getTimestamp("created_at").toLocalDateTime()
    );

    public Page<PostListDto>    findAllPostingWithNickname(Pageable pageable) {
        String sql = """
                SELECT p.id, p.title, p.view_count, p.created_at, u.nickname
                FROM post p
                JOIN users u ON p.author_id = u.id
                ORDER BY p.id ASC
                LIMIT ? OFFSET ?
                """;

        List<PostListDto> contents = jdbcTemplate.query(sql, postListRowMapper, pageable.getPageSize(), pageable.getOffset()
        );

        long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM post", Long.class);

        return new PageImpl<>(contents, pageable, total);
    }

    // 검색
    public Page<PostListDto> findByTitleWithNickname(String keyword, Pageable pageable) {
        String sql = """
            SELECT p.id, p.title, p.view_count, p.created_at, u.nickname
            FROM post p
            JOIN users u ON p.author_id = u.id
            WHERE p.title LIKE ?
            ORDER BY p.id ASC
            LIMIT ? OFFSET ?
            """;

        List<PostListDto> contents = jdbcTemplate.query(sql, postListRowMapper, "%" + keyword + "%", pageable.getPageSize(), pageable.getOffset()
    );

        long total = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM post WHERE title LIKE ?", Long.class, "%" + keyword + "%"
        );

        return new PageImpl<>(contents, pageable, total);
    }

    // 인기글
    public Page<PostListDto> findPopularWithNickname(Pageable pageable) {
        String sql = """
            SELECT p.id, p.title, p.view_count, p.created_at, u.nickname
            FROM post p
            JOIN users u ON p.author_id = u.id
            ORDER BY p.view_count DESC
            LIMIT ? OFFSET ?
            """;

        List<PostListDto> contents = jdbcTemplate.query(sql, postListRowMapper, pageable.getPageSize(), pageable.getOffset()
        );

        long total = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM post", Long.class);

        return new PageImpl<>(contents, pageable, total);
    }
}
