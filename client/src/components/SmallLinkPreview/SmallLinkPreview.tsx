import React, {FC, ReactElement} from 'react';

import {useSmallLinkPreviewStyles} from "./SmallLinkPreviewStyles";
import {Tweet} from "../../store/ducks/tweets/contracts/state";
import {LinkIcon, PlayVideoIcon} from "../../icons";

interface SmallLinkPreviewProps {
    tweet: Tweet;
    onOpenYouTubeVideo?: () => void;
    isFullTweet?: boolean;
}

const SmallLinkPreview: FC<SmallLinkPreviewProps> = ({tweet, onOpenYouTubeVideo, isFullTweet}): ReactElement => {
    const classes = useSmallLinkPreviewStyles({linkCover: tweet.linkCover, isFullTweet: isFullTweet});
    const matches = tweet.link.match(/^https?\:\/\/([^\/?#]+)(?:[\/?#]|$)/i);
    const domain = matches && matches[1];

    const LinkPreview = (): JSX.Element => {
        if (onOpenYouTubeVideo) {
            return (
                <div className={classes.container} onClick={onOpenYouTubeVideo}>
                    <div className={classes.linkPreviewImage}>
                        <div className={classes.videoIcon}>
                            {PlayVideoIcon}
                        </div>
                    </div>
                    <LinkPreviewInfo/>
                </div>
            );
        } else {
            return (
                <a className={classes.siteLink} target="_blank" href={tweet.link}>
                    <div className={classes.container}>
                        <div className={classes.linkPreviewImage}/>
                        <LinkPreviewInfo/>
                    </div>
                </a>
            );
        }
    };

    const LinkPreviewInfo = (): JSX.Element => {
        return (
            <div className={classes.linkPreviewTitle}>
                <div className={classes.linkTitle}>
                    {tweet.linkTitle}
                </div>
                <div className={classes.linkDescription}>
                    {tweet.linkDescription}
                </div>
                <div className={classes.link}>
                    {LinkIcon}{domain}
                </div>
            </div>
        );
    };

    return (<LinkPreview/>);
};

export default SmallLinkPreview;
