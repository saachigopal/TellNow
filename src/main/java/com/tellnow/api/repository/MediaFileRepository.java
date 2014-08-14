package com.tellnow.api.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tellnow.api.domain.MediaFile;

public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {

	MediaFile findByMd5Sum(String md5);
	
	MediaFile findByPathAndMd5Sum(String path, String md5sum);

	@Query("select m.id from MediaFile m where m.md5Sum = :md5Sum")
	Long getId(@Param("md5Sum") String md5Sum);
	
	@Query(value = "SELECT * FROM media_file AS m WHERE "
			+ "m.id NOT IN (SELECT media_id FROM answer_media) AND "
			+ "m.id NOT IN (SELECT media_id FROM question_media)", nativeQuery = true)
	Set<MediaFile> findOrphaned();
}
