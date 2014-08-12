package com.tellnow.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tellnow.api.domain.MediaFile;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

	MediaFile findByMd5Sum(String md5);

	@Query("select m.id from MediaFile m where m.md5Sum = :md5Sum")
	Long getId(@Param("md5Sum") String md5Sum);
}
