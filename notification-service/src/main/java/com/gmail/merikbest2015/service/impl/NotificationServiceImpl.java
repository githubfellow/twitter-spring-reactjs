package com.gmail.merikbest2015.service.impl;

import com.gmail.merikbest2015.dto.HeaderResponse;
import com.gmail.merikbest2015.dto.response.tweet.TweetResponse;
import com.gmail.merikbest2015.dto.response.user.UserResponse;
import com.gmail.merikbest2015.dto.request.IdsRequest;
import com.gmail.merikbest2015.dto.response.notification.NotificationListResponse;
import com.gmail.merikbest2015.dto.response.notification.NotificationTweetResponse;
import com.gmail.merikbest2015.dto.response.notification.NotificationUserResponse;
import com.gmail.merikbest2015.exception.ApiRequestException;
import com.gmail.merikbest2015.feign.ListsClient;
import com.gmail.merikbest2015.feign.TweetClient;
import com.gmail.merikbest2015.feign.UserClient;
import com.gmail.merikbest2015.repository.NotificationRepository;
import com.gmail.merikbest2015.repository.projection.NotificationInfoProjection;
import com.gmail.merikbest2015.repository.projection.NotificationProjection;
import com.gmail.merikbest2015.service.NotificationService;
import com.gmail.merikbest2015.util.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.gmail.merikbest2015.constants.ErrorMessage.NOTIFICATION_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserClient userClient;
    private final TweetClient tweetClient;
    private final ListsClient listsClient;

    @Override
    @Transactional
    public Page<NotificationProjection> getUserNotifications(Pageable pageable) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        userClient.resetNotificationCount();
        return notificationRepository.getNotificationsByUserId(authUserId, pageable);
    }

    @Override
    @Transactional
    public List<NotificationUserResponse> getTweetAuthorsNotifications() {
        userClient.resetNotificationCount();
        return userClient.getUsersWhichUserSubscribed();
    }

    @Override
    public NotificationInfoProjection getUserNotificationById(Long notificationId) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        return notificationRepository.getUserNotificationById(authUserId, notificationId)
                .orElseThrow(() -> new ApiRequestException(NOTIFICATION_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    @Override
    public HeaderResponse<TweetResponse> getNotificationsFromTweetAuthors(Pageable pageable) {
        Long authUserId = AuthUtil.getAuthenticatedUserId();
        List<Long> userIds = userClient.getUserIdsWhichUserSubscribed();
        List<Long> tweetIds = notificationRepository.getTweetIdsByNotificationType(userIds, authUserId);
        return tweetClient.getTweetsByIds(new IdsRequest(tweetIds), pageable);
    }

    public UserResponse getUserById(Long userId) {
        return userClient.getUserById(userId);
    }

    public TweetResponse getTweetById(Long tweetId) {
        return tweetClient.getTweetById(tweetId);
    }

    public NotificationUserResponse getNotificationUser(Long userId) {
        return userClient.getNotificationUser(userId);
    }

    public NotificationTweetResponse getNotificationTweet(Long userId) {
        return tweetClient.getNotificationTweet(userId);
    }

    public NotificationListResponse getNotificationList(Long listId) {
        return listsClient.getNotificationList(listId);
    }
}
