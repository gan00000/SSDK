package com.starpy.sdk.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.core.base.utils.PL;
import com.starpy.base.bean.SGameLanguage;
import com.starpy.base.bean.SPayType;
import com.starpy.data.login.ILoginCallBack;
import com.starpy.data.login.response.SLoginResponse;
import com.starpy.data.login.response.User;
import com.starpy.sdk.out.IPayCallBack;
import com.starpy.sdk.out.IRequestUserCallBack;
import com.starpy.sdk.out.ISdkCallBack;
import com.starpy.sdk.out.IStarpy;
import com.starpy.sdk.out.StarpyFactory;
import com.starpy.thirdlib.facebook.FriendProfile;
import com.starpy.thirdlib.facebook.SFacebookProxy;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button loginButton, othersPayButton,googlePayBtn,csButton,shareButton;

    List<FriendProfile> mFriendProfiles;

    private IStarpy iStarpy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginButton = (Button) findViewById(R.id.demo_login);
        othersPayButton = (Button) findViewById(R.id.demo_pay);
        googlePayBtn = (Button) findViewById(R.id.demo_pay_google);
        csButton = (Button) findViewById(R.id.demo_cs);
        shareButton = (Button) findViewById(R.id.demo_share);

        iStarpy = StarpyFactory.create();

        iStarpy.setGameLanguage(this, SGameLanguage.zh_CH);

        //初始化sdk
        iStarpy.initSDK(this);

        //在游戏Activity的onCreate生命周期中调用
        iStarpy.onCreate(this);


        /**
         * 在游戏获得角色信息的时候调用
         * roleId 角色id
         * roleName  角色名
         * rolelevel 角色等级
         * severCode 角色伺服器id
         * serverName 角色伺服器名称
         */
        iStarpy.registerRoleInfo(this, "roleid_1", "roleName", "rolelevel", "1000", "serverName");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //登陆接口 ILoginCallBack为登录成功后的回调
                iStarpy.login(MainActivity.this, new ILoginCallBack() {
                    @Override
                    public void onLogin(SLoginResponse sLoginResponse) {
                        if (sLoginResponse != null){
                            String uid = sLoginResponse.getUserId();
                            String accessToken = sLoginResponse.getAccessToken();
                            String timestamp = sLoginResponse.getTimestamp();

                            String fbName = sLoginResponse.getFbName();//fb昵称
                            String fbPictureUrl = sLoginResponse.getFbPictureUrl();//fb头像url

                            PL.i("uid:" + uid);
                        }
                    }
                });

            }
        });


        othersPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /*
                充值接口
                SPayType SPayType.OTHERS为第三方储值，SPayType.GOOGLE为Google储值
                cpOrderId cp订单号，请保持每次的值都是不会重复的
                productId 充值的商品id
                roleLevel 觉得等级
                customize 自定义透传字段（从服务端回调到cp）
                */
                iStarpy.pay(MainActivity.this, SPayType.OTHERS, new IPayCallBack() {
                    @Override
                    public void onFinish() {
                        PL.d("other pay onFinish");
                    }
                },"", "", "", "");


            }
        });

        googlePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                充值接口
                SPayType SPayType.OTHERS为第三方储值，SPayType.GOOGLE为Google储值
                cpOrderId cp订单号，请保持每次的值都是不会重复的
                productId 充值的商品id
                roleLevel 觉得等级
                customize 自定义透传字段（从服务端回调到cp）
                */
                iStarpy.pay(MainActivity.this, SPayType.GOOGLE, new IPayCallBack() {
                    @Override
                    public void onFinish() {
                        PL.d("google pay onFinish");
                    }
                },"" + System.currentTimeMillis(), "tw.mthx.100usd", "roleLevel", "customize");

            }
        });

        csButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 打开客服接口
                 * level：游戏等级
                 * vipLevel：vip等级，没有就传""
                 */
                iStarpy.cs(MainActivity.this,"level","vipLevel");
            }
        });



        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //下面的参数请按照实际传值
                String shareUrl = "http://ads.starb168.com/ads_scanner?gameCode=mthxtw&adsPlatForm=star_event&advertiser=share";
                //分享回调
                ISdkCallBack iSdkCallBack = new ISdkCallBack() {
                    @Override
                    public void success() {
                        PL.i("share  success");
                    }

                    @Override
                    public void failure() {
                        PL.i("share  failure");
                    }
                };

                iStarpy.share(MainActivity.this,iSdkCallBack,shareUrl);

            }
        });

        findViewById(R.id.open_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 打开一个活动页面接口
                 * level：游戏等级
                 * vipLevel：vip等级，没有就写""
                 */
                iStarpy.openWebview(MainActivity.this,"roleLevel","10");

            }
        });

        findViewById(R.id.open_plat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 打开内置平台页面
                 * level：游戏等级
                 * vipLevel：vip等级，没有就写""
                 */
                iStarpy.openPlatform(MainActivity.this,"roleLevel","10");

            }
        });

        findViewById(R.id.demo_google_unlock).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 解锁成就
                 * 参数：
                 * 成就 id
                 */
                iStarpy.unlockAchievement("CgkIq8GizdAREAIQAA");
            }
        });
        findViewById(R.id.demo_dis_cj).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 显示成就
                 */
                iStarpy.displayingAchievements();
            }
        });
        findViewById(R.id.open_sumitScore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 提交排行榜成绩
                 *
                 * 参数：
                 *  排行榜id
                 *  成绩分数
                 */
                iStarpy.submitScore("CgkIq8GizdAREAIQHg",10l);
            }
        });
        findViewById(R.id.open_dis_phb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 显示排行榜
                 * 参数：
                 *  排行榜id
                 */
                iStarpy.displayLeaderboard("CgkIq8GizdAREAIQHg");
            }
        });

        findViewById(R.id.get_fb_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 获取我的fb好友
                 */
                iStarpy.getFacebookFriends(MainActivity.this, new SFacebookProxy.RequestFriendsCallBack() {
                    @Override
                    public void onError() {

                    }

                    /**
                     * 回调friendProfiles为好友对象列表
                     * @param graphObject
                     * @param friendProfiles
                     */
                    @Override
                    public void onSuccess(JSONObject graphObject, List<FriendProfile> friendProfiles) {
                        for (FriendProfile fbFriend:friendProfiles) {
                            String name = fbFriend.getName();//FB好有名字
                            String iconUrl = fbFriend.getIconUrl();//FB好友头像url
                            String idToken = fbFriend.getId();//好友的fbIdToken
                            String userId = fbFriend.getUserId();//好友在starpy平台的账号id,即好友在玩该游戏的starpy平台的账号id，如果fb好友没有玩，此字段为NULL
                            boolean isStarpyUser = fbFriend.isStarpyUser();//可以判断该好友是否是starpy平台的用户，即是否玩过该游戏
                        }


                    }
                });
            }
        });

        findViewById(R.id.invite_fb_friends).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 邀请好友
                 * mFriendProfiles   需要邀请的好友对象列表(已经进入过该游戏的好友不可以再邀请，需要过滤，不给玩家选择)
                 * message  发送邀请的内容
                 * FbInviteFriendsCallBack  邀请回调
                 */
                iStarpy.inviteFriends(MainActivity.this, mFriendProfiles, "好玩哦", new SFacebookProxy.FbInviteFriendsCallBack() {
                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onSuccess(String requestId, List<String> requestRecipients) {
                        //里面回传的参数不需要处理
                    }
                });

            }
        });

        findViewById(R.id.showad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 获得通过fb分享连接安装进游戏的用户
                 */
                iStarpy.showAd(MainActivity.this, new SFacebookProxy.FbAdCallBack() {
                    @Override
                    public void onInterstitialDisplayed() {

                    }

                    @Override
                    public void onInterstitialDismissed() {

                    }

                    @Override
                    public void onAdClicked() {

                    }
                });

            }
        });

        findViewById(R.id.get_invite_user).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 获得通过fb分享连接安装进游戏的用户
                 */
                iStarpy.getInviteUser(MainActivity.this, new IRequestUserCallBack() {
                    @Override
                    public void onFinish(List<User> users) {
                        if (users != null && !users.isEmpty()){
                            //continue to do
                        }
                    }
                });

            }
        });
        findViewById(R.id.fb_share_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * 该接口为fb分享到达一定次数后弹出fb广告接口
                 * ISdkCallBack 为分享回调
                 * shareLinkUrl为分享的链接
                 * FbAdCallBack 为广告的回调
                 */
                iStarpy.fbShareAndShowAd(MainActivity.this,
                        new ISdkCallBack() {
                            @Override
                            public void success() {

                            }

                            @Override
                            public void failure() {

                            }
                        },
                        "https://developers.facebook.com/docs/facebook-login/review/examples/#example",

                        new SFacebookProxy.FbAdCallBack() {
                            @Override
                            public void onInterstitialDisplayed() {
                                //广告显示
                            }

                            @Override
                            public void onInterstitialDismissed() {
                                //广告消失
                            }

                            @Override
                            public void onAdClicked() {
                                //广告被点击
                            }
                        });

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        PL.i("activity onResume");
        iStarpy.onResume(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        iStarpy.onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        iStarpy.onPause(this);
        PL.i("activity onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        PL.i("activity onStop");
        iStarpy.onStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PL.i("activity onDestroy");
        iStarpy.onDestroy(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PL.i("activity onRequestPermissionsResult");
        iStarpy.onRequestPermissionsResult(this,requestCode,permissions,grantResults);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        iStarpy.onWindowFocusChanged(this,hasFocus);
    }
}
