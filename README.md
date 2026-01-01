# PlayerEntryTracking

BungeeCordプロキシサーバー用のプレイヤー接続通知プラグイン

## 概要

このプラグインは、BungeeCordプロキシサーバーに接続しているプレイヤーの行動を監視し、全員に通知メッセージを送信します。

## 機能

- 🟢 **参加通知**: プレイヤーがBungeeCordプロキシに接続すると「testさんが参加しました」と全員に通知
- 🔄 **移動通知**: プレイヤーがサーバー間を移動すると「testさんが02サーバーへ移動しました」と全員に通知
- 🔴 **退出通知**: プレイヤーがBungeeCordプロキシから切断すると「testさんが退出しました」と全員に通知
- ⚙️ **カスタマイズ可能**: config.ymlでメッセージや色を自由にカスタマイズ可能

## 動作環境

- **Minecraft Java版**: 1.19.4
- **BungeeCord**: 1.19-R0.1-SNAPSHOT以降
- **Java**: 17以上
- **開発環境**: IntelliJ IDEA 2025.3.1
- **ビルドツール**: Gradle 8.5

## インストール方法

1. このリポジトリをクローンまたはダウンロードします
2. プロジェクトをビルドします:
   ```bash
   ./gradlew build
   ```
3. 生成されたJARファイル（`build/libs/PlayerEntryTracking-1.0.0.jar`）をBungeeCordサーバーの`plugins`フォルダにコピーします
4. BungeeCordサーバーを起動または再起動します

## ビルド方法

### Gradleコマンドでビルド
```bash
# Windowsの場合
gradlew.bat build

# Linux/Macの場合
./gradlew build
```

### IntelliJ IDEAでビルド
1. IntelliJ IDEAでプロジェクトを開きます
2. 右側の「Gradle」タブを開きます
3. `Tasks` → `build` → `build` をダブルクリック

ビルドが成功すると、`build/libs/`フォルダにJARファイルが生成されます。

## 開発環境のセットアップ

1. IntelliJ IDEA 2025.3.1をインストール
2. このプロジェクトを開く（`File` → `Open`）
3. Gradleの自動インポートを待つ
4. JDK 17が設定されていることを確認（`File` → `Project Structure` → `Project`）

## 使用方法

プラグインをインストールした後、初回起動時に自動的に `plugins/PlayerEntryTracking/config.yml` が生成されます。

### 基本的な使い方

デフォルト設定でそのまま使用できます。プラグインは自動的に以下のイベントを監視します：

1. **プレイヤーの参加**: BungeeCordプロキシに接続した時
2. **サーバー移動**: プロキシ内の別のサーバーに移動した時
3. **プレイヤーの退出**: BungeeCordプロキシから切断した時

### メッセージのカスタマイズ

`plugins/PlayerEntryTracking/config.yml` を編集してメッセージをカスタマイズできます：

```yaml
messages:
  join: "&a[+] &f{player}"
  switch: "&b{player} &7→ &e{server}"
  quit: "&c[-] &f{player}"
```

**使用可能なプレースホルダー:**
- `{player}` - プレイヤー名
- `{server}` - サーバー名（移動メッセージのみ）

**カラーコード:**
- `&0-9`, `&a-f` - 色
- `&l` - 太字、`&m` - 取り消し線、`&n` - 下線、`&o` - 斜体
- `&r` - リセット

### 通知の有効/無効

特定の通知を無効にすることもできます：

```yaml
notifications:
  join: true    # 参加通知
  switch: false # サーバー移動通知を無効化
  quit: true    # 退出通知
```

📖 **詳しい使い方やトラブルシューティングは [使い方ガイド（USAGE_GUIDE.md）](USAGE_GUIDE.md) をご覧ください。**

## プロジェクト構造

```
PlayerEntryTracking/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/github/kubota6646/playerentrytracking/
│       │       ├── PlayerEntryTracking.java         # メインプラグインクラス
│       │       ├── PlayerConnectionListener.java    # イベントリスナー
│       │       └── PluginConfig.java                # 設定管理クラス
│       └── resources/
│           ├── bungee.yml                           # プラグイン設定ファイル
│           ├── config.yml                           # メッセージ設定ファイル
│           └── config.yml.example                   # 設定例
├── build.gradle                                     # Gradle設定ファイル
├── settings.gradle                                  # Gradleプロジェクト設定
└── README.md                                        # このファイル
```

## ライセンス

このプロジェクトはMITライセンスの下で公開されています。詳細は[LICENSE](LICENSE)ファイルを参照してください。

## 作者

kubota6646

## サポート

問題や提案がある場合は、GitHubのIssuesセクションで報告してください。