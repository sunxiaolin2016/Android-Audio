# Android-Audio
1.CarAudioTrack Demo,用来演示AudioTrack的使用方法，主要实现了用AudioTrack播放pcm或者wav格式的音频文件。

Android P默认音量值修改：
1.修改frameworks\base\media\java\android\media\AudioSystem.java中的DEFAULT_STREAM_VOLUME;
2.参照frameworks\base\services\core\java\com\android\server\audio\AudioService.java中的MAX_STREAM_VOLUME;
